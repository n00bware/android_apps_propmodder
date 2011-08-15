#!/system/bin/sh
BB=/system/xbin/busybox
TMP_DIR=/tmp
TMP_FILE=/tmp/pm_build.prop
BUILDPROP=/system/build.prop
FUNCTION=$1
NEWVALUE=$2

apply()
{
cp -f $TMP_FILE $BUILDPROP
rm -f $TMP_FILE
}

reboot()
{
rm -f $TMP_FILE
}

apply_reboot()
{
cp -f $TMP_FILE $BUILDPROP
rm -f $TMP_FILE
reboot
}

wifi()
{
$BB sed -i "/wifi.supplicant_scan_interval/ c wifi.supplicant_scan_interval = $1" $TMP_FILE
}

lcd()
{
$BB sed -i "/ro.sf.lcd_density/ c ro.sf.lcd_density=$1" $TMP_FILE
}

max_events()
{
$BB sed -i "/windowsmgr.max_events_per_sec/ c windowsmgr.max_events_per_sec=$1" $TMP_FILE
}

usb_mode()
{
$BB sed -i "/ro.default_usb_mode/ c ro.default_usb_mode=$1" $TMP_FILE
}

ring_delay()
{
$BB sed -i "/ro.telephony.call_ring.delay/ c ro.telephony.call_ring.delay=$1" $TMP_FILE
}

vm_heapsize()
{
$BB sed -i "/dalvik.vm.heapsize/ c dalvik.vm.heapsize=$1" $TMP_FILE
}

mod_version()
{
$BB sed -i "/ro.modversion/ c ro.modversion=$@" $TMP_FILE
}

if $BB [ ! -e /tmp/pm_build.prop ]
  then
    cp -f $BUILDPROP $TMP_FILE
fi

if $BB [[ "$FUNCTION" = "mod_version" ]]
  then
    NEWVALUE="$(echo $@ | sed 's/mod_version//')"
fi
echo "FUNCTION = $FUNCTION"
echo "NEWVALUE = $NEWVALUE"
$FUNCTION $NEWVALUE