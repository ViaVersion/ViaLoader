/*
 * This file is part of ViaProtocolHack - https://github.com/RaphiMC/ViaProtocolHack
 * Copyright (C) 2023 RK_01/RaphiMC and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.raphimc.viaprotocolhack.impl.viaversion;

import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.libs.fastutil.ints.IntLinkedOpenHashSet;
import com.viaversion.viaversion.libs.fastutil.ints.IntSortedSet;
import com.viaversion.viaversion.libs.gson.JsonObject;
import net.raphimc.viaprotocolhack.netty.VPHPipeline;
import net.raphimc.viaprotocolhack.util.VersionEnum;

public class VPHInjector implements ViaInjector {

    @Override
    public void inject() {
    }

    @Override
    public void uninject() {
    }

    @Override
    public int getServerProtocolVersion() {
        return this.getServerProtocolVersions().firstInt();
    }

    @Override
    public IntSortedSet getServerProtocolVersions() {
        final IntSortedSet versions = new IntLinkedOpenHashSet();
        for (VersionEnum version : VersionEnum.OFFICIAL_SUPPORTED_PROTOCOLS) {
            versions.add(version.getOriginalVersion());
        }
        return versions;
    }

    @Override
    public String getEncoderName() {
        return VPHPipeline.ENCODER_HANDLER_NAME;
    }

    @Override
    public String getDecoderName() {
        return VPHPipeline.DECODER_HANDLER_NAME;
    }

    @Override
    public JsonObject getDump() {
        return new JsonObject();
    }

}
