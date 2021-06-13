Future
==============

* LizardNet Code Review to GitHub Mirroring Status: [![LizardNet Code Review to GitHub Mirroring Status](https://integration.fastlizard4.org:444/jenkins/buildStatus/icon?job=craftbukkit-plugins-Future%20github%20mirror)](https://integration.fastlizard4.org:444/jenkins/job/craftbukkit-plugins-Future%20github%20mirror/)
* Jenkins Autobuild Status: [![Jenkins Autobuild Status](https://integration.fastlizard4.org:444/jenkins/buildStatus/icon?job=craftbukkit-plugins-Future)](https://integration.fastlizard4.org:444/jenkins/job/craftbukkit-plugins-Future/)

**Warning: This plugin is still in development.  Use at your own risk.**

*A plugin for CraftBukkit-based Minecraft servers.*

Background
--------------------------
*Future* is a plugin to backport a selection of features from future versions of Minecraft.

Getting the Plugin and Builds
-----------------------------
Builds of the project can be acquired at our Jenkins Continuous Integration server, specifically at
[this project][jenkins-build].  If you want to download a build, you can get them at the
[Jenkins download page][jenkins-download], where builds are marked as follows:

* Development (red star): Build reviewed by developers and confirmed to at least
  run, but may be extremely buggy and break in other ways.  Not recommended for
  use in production environments
* Beta (orange star): Build tested to some degree, most obvious bugs already
  handled but some may be left.
* Stable/recommended (green star): Build thoroughly tested by devs, recommended
  for use on production servers.

Downloading a build not marked as one of these is not recommended, as these
may very well simply refuse to work and have not been tested *at all*!

There is also a Jenkins job that handles mirroring of the Git repository from
LizardNet Code Review to GitHub, and that can be found [here][jenkins-mirror].

Licensing and Ackowledgements
-----------------------------
**Future**

by Andrew "FastLizard4" Adams and the LizardNet CraftBukkit Plugins Development Team (see AUTHORS.txt file)

Copyright (C) 2021 by Andrew "FastLizard4" Adams and the LizardNet CraftBukkit Plugins Development Team. Some rights
reserved.

License GPLv3+: GNU General Public License version 3 or later (at your choice): <http://gnu.org/licenses/gpl.html>. This
is free software: you are free to  change and redistribute it at your will provided that your redistribution, with or
without modifications, is also licensed under the GNU GPL. (Although not required by the license, we also ask that you
attribute us!) There is **NO WARRANTY FOR THIS SOFTWARE** to the extent permitted by law.

[BukkitDev]: http://dev.bukkit.org/bukkit-plugins/future/
[lizardnet-repo]: https://git.fastlizard4.org/gitblit/summary/?r=craftbukkit-plugins/Future.git
[gerrit]: https://gerrit.fastlizard4.org
[gerrit-repo]: https://gerrit.fastlizard4.org/r/gitweb?p=craftbukkit-plugins/Future.git;a=summary
[github-repo]: https://github.com/LizardNet/craftbukkit-plugins-Future
[jenkins-build]: https://integration.fastlizard4.org:444/jenkins/job/craftbukkit-plugins-Future/
[jenkins-download]: https://integration.fastlizard4.org:444/jenkins/job/craftbukkit-plugins-Future/promotion/
[jenkins-mirror]: https://integration.fastlizard4.org:444/jenkins/job/craftbukkit-plugins-Future%20github%20mirror/
	