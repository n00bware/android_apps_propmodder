
package com.n00bware.propmodder;

public final class Constants {

    private Constants() {
        // private constructor so nobody can instantiate this class
    }

    /*
     * Strings for wifi_scan
     */
    public static final String WIFI_SCAN_PREF = "pref_wifi_scan_interval";

    public static final String WIFI_SCAN_PROP = "wifi.supplicant_scan_interval";

    public static final String WIFI_SCAN_PERSIST_PROP = "persist.wifi_scan_interval";

    public static final String WIFI_SCAN_DEFAULT = System.getProperty(WIFI_SCAN_PROP);

    /*
     * Strings for lcd_density
     */
    public static final String LCD_DENSITY_PREF = "pref_lcd_density";

    public static final String LCD_DENSITY_PROP = "ro.sf.lcd_density";

    public static final String LCD_DENSITY_PERSIST_PROP = "persist.lcd_density";

    public static final String LCD_DENSITY_DEFAULT = System.getProperty(LCD_DENSITY_PROP);

    /*
     * Strings for max_events
     */
    public static final String MAX_EVENTS_PREF = "pref_max_events";

    public static final String MAX_EVENTS_PROP = "windowsmgr.max_events_per_sec";

    public static final String MAX_EVENTS_PERSIST_PROP = "persist.max_events";

    public static final String MAX_EVENTS_DEFAULT = System.getProperty(MAX_EVENTS_PROP);

    /*
     * Strings for usb_mode
     */
    public static final String USB_MODE_PREF = "pref_usb_mode";

    public static final String USB_MODE_PROP = "ro.default_usb_mode";

    public static final String USB_MODE_PERSIST_PROP = "persist.usb_mode";

    public static final String USB_MODE_DEFAULT = System.getProperty(USB_MODE_PROP);

    /*
     * Strings for ring_delay
     */
    public static final String RING_DELAY_PREF = "pref_ring_delay";

    public static final String RING_DELAY_PROP = "ro.telephony.call_ring.delay";

    public static final String RING_DELAY_PERSIST_PROP = "persist.call_ring.delay";

    public static final String RING_DELAY_DEFAULT = System.getProperty(RING_DELAY_PROP);

    /*
     * Strings for vm_heapsize
     */
    public static final String VM_HEAPSIZE_PREF = "pref_vm_heapsize";

    public static final String VM_HEAPSIZE_PROP = "dalvik.vm.heapsize";

    public static final String VM_HEAPSIZE_PERSIST_PROP = "persist.vm_heapsize";

    public static final String VM_HEAPSIZE_DEFAULT = System.getProperty(VM_HEAPSIZE_PROP);

    /*
     * Strings for modversion
     */
    public static final String MOD_VERSION_PREF = "pref_modversion";

    public static final String MOD_VERSION_PROP = "ro.modversion";

    public static final String MOD_VERSION_PERSIST_PROP = "persist.modversion";

    public static final String MOD_VERSION_DEFAULT = System.getProperty(MOD_VERSION_PROP);

}
