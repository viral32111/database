# Database

This is a [Minecraft Fabric](https://fabricmc.net/) mod that stores player information in an external [MongoDB](https://www.mongodb.com/) database for other systems to utilise, such as statistics calculation.

This mod depends on [my Events mod](https://github.com/viral32111/events) for mixin callbacks.

## Usage

1. Download the JAR file from [the latest release](https://github.com/viral32111/database/releases/latest).
2. Download [my Events mod](https://github.com/viral32111/events).
3. Place the JAR files in the server's `mods` directory.
4. Start the server to initialise the mod for the first time.
5. Configure appropriately in the `config/viral32111/database.json` file.
6. Restart the server.

## License

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
