
package com.n00bware.propmodder;

public final class Constants {

    private Constants() {
        // private constructor so nobody can instantiate this class
    }

    public static final String SHOWBUILD_PATH = "/system/tmp/showbuild";

    public static final String DISABLE = "disable";

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
     * Strings for HSUPA (faster upload speed)
     */
    public static final String FAST_UP_PREF = "pref_fast_up";

    public static final String FAST_UP_PROP = "ro.ril.hsxpa";

    public static final String FAST_UP_PROP_DISABLE = "#ro.ril.hsxpa";

    public static final String FAST_UP_PERSIST_PROP = "persist.fast_up";

    public static final String FAST_UP_DEFAULT = System.getProperty(FAST_UP_PROP);

    /*
     * Strings for disabling the boot animation
     */
    public static final String DISABLE_BOOT_ANIM_PREF = "pref_disable_boot_anim";

    public static final String DISABLE_BOOT_ANIM_PROP_1 = "ro.kernel.android.bootanim";

    public static final String DISABLE_BOOT_ANIM_PROP_2 = "debug.sf.nobootanimation";

    public static final String DISABLE_BOOT_ANIM_PERSIST_PROP = "persist.disable_boot_anim";

    /*
     * Strings for proximity delay
     */
    public static final String PROX_DELAY_PREF = "pref_prox_delay";

    public static final String PROX_DELAY_PROP = "mot.proximity.delay";

    public static final String PROX_DELAY_PROP_DISABLE = "#mot.proximity.delay";

    public static final String PROX_DELAY_PERSIST_PROP = "persist.prox.delay";

    public static final String PROX_DELAY_DEFAULT = System.getProperty(PROX_DELAY_PROP);

}
