/*
 * Copyright (C) 2021 The OmniROM Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.omnirom.device;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import com.android.internal.util.omni.OmniUtils;

public class GameTile extends TileService {

    private static final String BROADCAST_ACTION_LAUNCH_PANEL = "com.asus.gamewidget.action.WIDGET_BTN_CLICKED";
    private static final String EXTRA_LAUNCH_PANEL_DISPLAY = "clicked_source";
    private static final String EXTRA_LAUNCH_PANEL_SIDE = "side";

    @Override
    public void onStartListening() {
        super.onStartListening();

        getQsTile().setIcon(Icon.createWithResource(this, R.drawable.game_genie));
        getQsTile().setState(Tile.STATE_ACTIVE);
        getQsTile().updateTile();
    }

    @Override
    public void onClick() {
        super.onClick();

        Intent intent = new Intent(BROADCAST_ACTION_LAUNCH_PANEL);
        intent.setPackage("com.asus.gamewidget");
        intent.putExtra(EXTRA_LAUNCH_PANEL_DISPLAY, 1);
        intent.putExtra(EXTRA_LAUNCH_PANEL_SIDE, true);
        //intent.putExtra("display_mode", 0);
        intent.addFlags(268435456);
        OmniUtils.sendKeycode(852);
        this.sendBroadcast(intent);
    }
}
