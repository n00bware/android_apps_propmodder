/*
 * Copyright (C) 2011 The CyanogenMod Project
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

package com.n00bware.propmodder;

import com.n00bware.propmodder.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Toast;
import java.*;
import android.*;

public class MainActivity extends PreferenceActivity {
        

    @Override
    public void onCreate(Bundle savedInstanceState) {

    Toast msg = Toast.makeText(MainActivity.this, "Starting MainActivity", Toast.LENGTH_SHORT);
    msg.show();
     
     super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.propmodder);
    }
}
