DESCRIPTION = "Board custom INIT file"
SECTION = "console/utils"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${RESIN_COREBASE}/COPYING.Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

SRC_URI = "file://resin-init-board"
S = "${WORKDIR}"

inherit allarch

FILES_${PN} = "${base_sbindir}/*"
RDEPENDS_${PN} = "bash"

do_install() {
        install -d ${D}${base_sbindir}
        install -m 0755 ${WORKDIR}/resin-init-board  ${D}${base_sbindir}
}
