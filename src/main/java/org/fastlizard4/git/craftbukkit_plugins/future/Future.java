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

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class Future extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Enabling future....");

        for (Recipe recipe : Recipes.getRecipes()) {
            if (getServer().addRecipe(recipe)) {
                getLogger().info("Successfully added recipe that produces: " + recipe.getResult().toString());
            } else {
                getLogger().severe("Failed to add recipe that produces: " + recipe.getResult().toString());
                getPluginLoader().disablePlugin(this);
            }
        }

        getLogger().info("Future enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling future....");

        Set<ItemStack> allRecipeOutputs = Recipes.getRecipes()
                .stream()
                .map(Recipe::getResult)
                .collect(Collectors.toSet());

        Iterator<Recipe> recipeList = getServer().recipeIterator();
        try {
            while (recipeList.hasNext()) {
                Recipe recipe;
                recipe = recipeList.next();

                if (allRecipeOutputs.contains(recipe.getResult())) {
                    recipeList.remove();
                    allRecipeOutputs.remove(recipe.getResult());
                }

                if (allRecipeOutputs.isEmpty()) {
                    break;
                }
            }
        } catch (Exception e) {
            getLogger().severe("Unable to unload a Future crafting recipe!  Expect undefined behavior!");
            getLogger().severe("The exception that occurred was: " + e);
            getLogger().severe("A stack trace will now be printed to stderr.");
            e.printStackTrace();
        }

        getLogger().info("Future disabled!");
    }
}
