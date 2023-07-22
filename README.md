# Database

[![CI](https://github.com/viral32111/database/actions/workflows/ci.yml/badge.svg)](https://github.com/viral32111/database/actions/workflows/ci.yml)
[![CodeQL](https://github.com/viral32111/database/actions/workflows/codeql.yml/badge.svg)](https://github.com/viral32111/database/actions/workflows/codeql.yml)
![GitHub tag (with filter)](https://img.shields.io/github/v/tag/viral32111/database?label=Latest)
![GitHub repository size](https://img.shields.io/github/repo-size/viral32111/database?label=Size)
![GitHub release downloads](https://img.shields.io/github/downloads/viral32111/database/total?label=Downloads)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/viral32111/database?label=Commits)

This is a [Minecraft Fabric](https://fabricmc.net/) mod that stores player information in an external [MongoDB](https://www.mongodb.com/) database for other systems to utilise, such as statistics calculation.

This mod depends on [my Events mod](https://github.com/viral32111/events) for mixin callbacks.

## 📥 Usage

<a href="https://modrinth.com/mod/fabric-api/"><img src="https://github.com/viral32111/discord-relay/assets/19510403/2e0d32ee-b4aa-4d93-9388-4a45639c4a96" height="48" alt="Requires Fabric API"></a>
<a href="https://modrinth.com/mod/fabric-language-kotlin"><img src="https://github.com/viral32111/discord-relay/assets/19510403/ab7b8cbb-ff80-4359-8fc9-13a2cf62c4bf" height="48" alt="Requires Fabric Language Kotlin"></a>
<br>

1. Download the JAR file from [the latest release](https://github.com/viral32111/database/releases/latest).
2. Download [my Events mod](https://github.com/viral32111/events).
3. Place the JAR files in the server's `mods` directory.
4. Start the server to initialise the mod for the first time.
5. Configure appropriately in the `config/viral32111/database.json` file.
6. Restart the server.

## ⚖️ License

Copyright (C) 2023 [viral32111](https://viral32111.com).

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program. If not, see https://www.gnu.org/licenses.
