/*
 * FUTURE
 * by Andrew "FastLizard4" Adams and the LizardNet CraftBukkit Plugins
 * Development Team (see AUTHORS.txt file)
 *
 * Copyright (C) 2021 by Andrew "FastLizard4" Adams and the LizardNet
 * CraftBukkit Plugins Development Team. Some rights reserved.
 *
 * License GPLv3+: GNU General Public License version 3 or later (at your choice):
 * <http://gnu.org/licenses/gpl.html>. This is free software: you are free to
 * change and redistribute it at your will provided that your redistribution, with
 * or without modifications, is also licensed under the GNU GPL. (Although not
 * required by the license, we also ask that you attribute us!) There is NO
 * WARRANTY FOR THIS SOFTWARE to the extent permitted by law.
 *
 * This is an open source project. The source Git repositories, which you are
 * welcome to contribute to, can be found here:
 * <https://gerrit.fastlizard4.org/r/gitweb?p=craftbukkit-plugins/Future.git;a=summary>
 * <https://git.fastlizard4.org/gitblit/summary/?r=craftbukkit-plugins/Future.git>
 *
 * Gerrit Code Review for the project:
 * <https://gerrit.fastlizard4.org/r/#/q/project:craftbukkit-plugins/Future,n,z>
 *
 * Continuous Integration for this project:
 * <https://integration.fastlizard4.org:444/jenkins/job/craftbukkit-plugins-Future/>
 *
 * Alternatively, the project source code can be found on the PUBLISH-ONLY mirror
 * on GitHub: <https://github.com/LizardNet/craftbukkit-plugins-Future>
 *
 * Note: Pull requests and patches submitted to GitHub will be transferred by a
 * developer to Gerrit before they are acted upon.
 */

package org.fastlizard4.git.craftbukkit_plugins.future;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.ImmutableSet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

public class MegaSprucePodzolListener implements Listener {

    private static final int RADIUS = 6;
    private static final int SEARCH_DELTA_Y = 4;
    private static final byte PODZOL_DATA_VALUE = 2;
    private static final Set<Material> TARGET_REPLACEABLE_BLOCK_TYPES = ImmutableSet
            .of(Material.MYCEL, Material.GRASS, Material.DIRT);

    private final Logger logger;

    public MegaSprucePodzolListener(Logger logger) {
        this.logger = Objects.requireNonNull(logger);
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onStructureGrow(StructureGrowEvent structureGrowEvent) {
        if (TreeType.MEGA_REDWOOD.equals(structureGrowEvent.getSpecies())) {
            List<BlockState> blocks = structureGrowEvent.getBlocks();

            if (blocks.stream()
                    .anyMatch(block -> Material.SAPLING.equals(block.getType()))) {
                return; // The tree did not spawn; there is nothing to do here.
            }

            Map<Integer, List<BlockState>> blocksGrouped = blocks.stream()
                    .collect(Collectors.groupingBy(
                            BlockState::getY,
                            Collectors.toList()
                    ));

            Optional<Entry<Integer, List<BlockState>>> lowestBlocksOpt = blocksGrouped.entrySet()
                    .stream()
                    .min(Comparator.comparingInt(Entry::getKey));

            if (!lowestBlocksOpt.isPresent()) {
                logger.warning("Could not identify lowest blocks of the new mega spruce. Bailing out.");
                return;
            }

            List<BlockState> lowestBlocks = lowestBlocksOpt.get().getValue();

            Optional<BlockState> southwestBlockOpt = lowestBlocks.stream()
                    .reduce(MegaSprucePodzolListener::getMostSouthwesterlyBlock);

            if (!southwestBlockOpt.isPresent()) {
                logger.warning(
                        "Could not identify most southwesterly block of dirt under the mega spruce. Bailing out.");
                return;
            }

            BlockState southwestBlock = southwestBlockOpt.get();

            Set<Location> circleLocations = getCircleLocations(
                    southwestBlock.getWorld(),
                    southwestBlock.getX(),
                    southwestBlock.getY(),
                    southwestBlock.getZ()
            );

            Set<Block> circleBlocks = getCircleSurfaceBlocks(circleLocations);

            circleBlocks.stream()
                    .filter(MegaSprucePodzolListener::isReplaceableBlock)
                    .forEach(block -> {
                        block.setType(Material.DIRT);
                        block.setData(PODZOL_DATA_VALUE);
                    });
        }
    }

    private static BlockState getMostSouthwesterlyBlock(BlockState block1, BlockState block2) {
        int block1X = block1.getX();
        int block1Z = block1.getZ();
        int block2X = block2.getX();
        int block2Z = block2.getZ();

        if (block2Z > block1Z || block2X < block1X) {
            return block2;
        } else {
            return block1;
        }
    }

    private static boolean isSurfaceBlock(Block block) {
        Block blockAbove = block.getRelative(BlockFace.UP);
        return !block.getType().isTransparent() && blockAbove.getType().isTransparent();
    }

    private static boolean isReplaceableBlock(Block block) {
        return TARGET_REPLACEABLE_BLOCK_TYPES.contains(block.getType());
    }

    private static Set<Location> getCircleLocations(World world, int centerX, int centerY, int centerZ) {
        Location center = new Location(world, centerX, centerY, centerZ);

        return IntStream.rangeClosed(centerX - RADIUS, centerX + RADIUS)
                .boxed()
                .flatMap(x -> IntStream.rangeClosed(centerZ - RADIUS, centerZ + RADIUS)
                        .mapToObj(z -> new Location(world, x, centerY, z))
                )
                .filter(loc -> loc.distance(center) < (RADIUS + 1))
                .collect(Collectors.toSet());
    }

    private static Set<Block> getCircleSurfaceBlocks(Set<Location> circleLocations) {
        Set<Block> circleBlocks = new HashSet<>();

        for (Location location : circleLocations) {
            Block block = location.getBlock();

            if (isSurfaceBlock(block)) {
                circleBlocks.add(block);
                continue;
            }

            Optional<Block> candidate = findSurfaceBlock(block);
            candidate.ifPresent(circleBlocks::add);
        }

        return circleBlocks;
    }

    private static Optional<Block> findSurfaceBlock(Block block) {
        Block searchStart = block.getRelative(BlockFace.UP, SEARCH_DELTA_Y);

        return IntStream.rangeClosed(0, SEARCH_DELTA_Y * 2)
                .mapToObj(i -> searchStart.getRelative(BlockFace.DOWN, i))
                .filter(MegaSprucePodzolListener::isSurfaceBlock)
                .findFirst();
    }
}
