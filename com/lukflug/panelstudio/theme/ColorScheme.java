package com.lukflug.panelstudio.theme;

import java.awt.Color;

public interface ColorScheme {
   Color getActiveColor();

   Color getInactiveColor();

   Color getBackgroundColor();

   Color getOutlineColor();

   Color getFontColor();

   int getOpacity();
}
