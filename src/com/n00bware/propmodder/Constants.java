
package com.n00bware.propmodder;

public final class Constants {

    private Constants() {
        // private constructor so nobody can instantiate this class
    }

    public static final String SHOWBUILD_PATH = "/system/tmp/showbuild";
    public static final String DISABLE = "disable";
    public static final String INIT_SCRIPT_PATH ="/system/etc/init.d/72propmodder_script";
    public static final String INIT_SCRIPT_TEMP_PATH = "/system/tmp/init_script";

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
    public static final String PROX_DELAY_PERSIST_PROP = "persist.prox.delay";
    public static final String PROX_DELAY_DEFAULT = System.getProperty(PROX_DELAY_PROP);

    /*
     * Strings for removing adb logcat log
     */
    public static final String LOGCAT_PREF = "pref_logcat";
    public static final String LOGCAT_PERSIST_PROP = "persist.logcat";
    public static final String LOGCAT_DISABLE = "#rm -f /dev/log/main";
    public static final String LOGCAT_ALIVE_PATH = "/system/etc/init.d/72propmodder_script";
    public static final String LOGCAT_ENABLE = "rm -f /dev/log/main";

    /*
     * Strings for removing adb logcat log
     */
    public static final String MOD_VERSION_PREF = "pref_mod_version";
    public static final String MOD_VERSION_PROP = "ro.build.display.id";
    public static final String MOD_VERSION_PERSIST_PROP = "persist.build.display.id";
    public static final String MOD_VERSION_DEFAULT = System.getProperty(MOD_VERSION_PROP);
    public static final String MOD_BUTTON_TEXT = "doMod";
    public static final String MOD_VERSION_TEXT = "Mods by PropModder";

    /*
     * Strings for Sleep mode
     */
    public static final String SLEEP_PREF = "pref_sleep";
    public static final String SLEEP_PROP = "pm.sleep_mode";
    public static final String SLEEP_PERSIST_PROP = "persist.sleep";
    public static final String SLEEP_DEFAULT = System.getProperty(SLEEP_PROP);


    /*
     * Strings for TCP Stack Optimizations
     */
    public static final String TCP_STACK_PREF = "pref_tcp_stack";
    public static final String TCP_STACK_PERSIST_PROP = "persist_tcp_stack";
    public static final String TCP_STACK_PROP_0 = "net.tcp.buffersize.default";
    public static final String TCP_STACK_PROP_1 = "net.tcp.buffersize.wifi";
    public static final String TCP_STACK_PROP_2 = "net.tcp.buffersize.umts";
    public static final String TCP_STACK_PROP_3 = "net.tcp.buffersize.gprs";
    public static final String TCP_STACK_PROP_4 = "net.tcp.buffersize.edge";
    public static final String TCP_STACK_BUFFER = "4096,87380,256960,4096,16384,256960";

    /*
     * Strings for jit compiler
     */
    public static final String JIT_PREF = "pref_jit";
    public static final String JIT_PERSIST_PROP = "persist_jit";
    public static final String JIT_PROP = "dalvik.vm.execution-mode";

    /*
     * Strings for disabling check in service
     */
    public static final String CHECK_IN_PREF = "pref_check_in";
    public static final String CHECK_IN_PERSIST_PROP = "persist_check_in";
    public static final String CHECK_IN_PROP = "ro.config.nocheckin";
    public static final String CHECK_IN_PROP_HTC = "ro.config.htc.nocheckin";

    /*
     * Strings for applying sdcard memory buffer hack #sdcard_speed_hack
     */
    public static final String SDCARD_BUFFER_PREF = "pref_sdcard_buffer";
    public static final String SDCARD_BUFFER_PRESIST_PROP = "persist_sdcard_buffer";

}
