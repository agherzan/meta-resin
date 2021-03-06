DESCRIPTION = "Resin Supervisor custom INIT file"
SECTION = "console/utils"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${RESIN_COREBASE}/COPYING.Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r3"

SRC_URI = " \
    file://supervisor-init \
    file://supervisor-init.service \
    "
S = "${WORKDIR}"

FILES_${PN} = "/resin-data /mnt/data-disk ${sysconfdir}/* ${base_bindir}/*"
RDEPENDS_${PN} = " \
    bash \
    connman \
    rce \
    rce-run-supervisor \
    resin-device-progress \
    resin-init \
    wireless-tools \
    resin-supervisor \
    socat \
    resin-conf"

do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_build[noexec] = "1"

# MIXPANEL TOKEN
MIXPANEL_TOKEN_PRODUCTION = "99eec53325d4f45dd0633abd719e3ff1"
MIXPANEL_TOKEN_STAGING = "cb974f32bab01ecc1171937026774b18"

inherit update-rc.d systemd

INITSCRIPT_NAME = "supervisor-init"
INITSCRIPT_PARAMS = "defaults 99"

SYSTEMD_SERVICE_${PN} = "supervisor-init.service"

do_install() {

    install -d ${D}/resin-data
    install -d ${D}/mnt/data-disk
    install -d ${D}${sysconfdir}/default
    install -d ${D}${sysconfdir}

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d ${D}${base_bindir}
        install -m 0755 ${WORKDIR}/supervisor-init ${D}${base_bindir}
        install -d ${D}${systemd_unitdir}/system
        install -c -m 0644 ${WORKDIR}/supervisor-init.service ${D}${systemd_unitdir}/system
        sed -i -e 's,@BASE_BINDIR@,${base_bindir},g' \
            -e 's,@SBINDIR@,${sbindir},g' \
            -e 's,@BINDIR@,${bindir},g' \
            ${D}${systemd_unitdir}/system/*.service
    else
        install -d ${D}${sysconfdir}/init.d/
        install -m 0755 ${WORKDIR}/supervisor-init  ${D}${sysconfdir}/init.d/supervisor-init
    fi

    if ${@bb.utils.contains('DISTRO_FEATURES','resin-staging','true','false',d)}; then
        # Staging Resin build

        if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
            sed -i -e 's:> /dev/null 2>&1::g' ${D}${base_bindir}/supervisor-init
        else
            sed -i -e 's:> /dev/null 2>&1::g' ${D}${sysconfdir}/init.d/supervisor-init
        fi
    fi

}
do_install[vardeps] += "DISTRO_FEATURES"
