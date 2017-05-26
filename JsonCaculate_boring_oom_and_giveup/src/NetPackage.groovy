import groovy.json.internal.LazyMap

/**
 * Created by sodino on 2017/5/22.
 */
class NetPackage {
    String line_number;
    String ip_src, ip_dst;
    int tcp_port_src, tcp_port_dst;
    String time;
    float time_relative // ms

    String type

    static final TCP_1 = "TCP_1"
    static final TCP_2 = "TCP_2"
    static final TCP_3 = "TCP_3"
    static String Client_Hello = "[Client Hello]"
    static String Certificate = "[Certificate]"
    static String Certificate_Status = "[Certificate Status]"
    static String SERVER_HELLO = "[Server Hello]"
    static String ServerKeyExchange = "[ServerKeyExchange]"
    static String New_Session_Ticket = "[New Session Ticket]"
    static String ServerHelloDone = "[ServerHelloDone]"
    static String ClientKeyExchange = "[ClientKeyExchange]"
    static String ChangeCipherSpec = "[ChangeCipherSpec]"
    static String FINISHED = "[Finished]"
    static String APP_DATA = "[Application Data]"


    NetPackage(LazyMap jsonItem) {
        line_number = jsonItem._source.layers.frame["frame.number"]

        type = ""
        if (isTcp1(jsonItem)) {
            type = NetPackage.TCP_1
        } else if (isTcp2(jsonItem)) {
            type = NetPackage.TCP_2
        } else if (isTcp3(jsonItem)) {
            type = NetPackage.TCP_3
        } else if (isClientHello(jsonItem)) {
            type = NetPackage.Client_Hello
        }
        if (isCertificate(jsonItem)) {
            type = NetPackage.Certificate
        }
        if (isServerHello(jsonItem)) {
            type += NetPackage.SERVER_HELLO
        }
        if (isCertificateStatus(jsonItem)) {
            type += NetPackage.Certificate_Status
        }
        if (isChangeCipherSpec(jsonItem)) {
            type += NetPackage.ChangeCipherSpec
        }
//        if (isServerKeyExchange(jsonItem)) {
//
//        }
//        if (isServerHelloDone(jsonItem)) {
//
//        }
        if (isClientKeyExchange(jsonItem)) {
            type += NetPackage.ClientKeyExchange
        }
        if (isAppData(jsonItem)) {
            type += NetPackage.APP_DATA
        }
        if (isNewSessionTicket(jsonItem)) {
            type += NetPackage.New_Session_Ticket
        }

        if (type.length() == 0) {
            type = "unknown"
        }

        ip_src = jsonItem._source.layers.ip["ip.src"]
        ip_dst = jsonItem._source.layers.ip["ip.dst"]

        tcp_port_src = jsonItem._source.layers.tcp["tcp.srcport"].toInteger()
        tcp_port_dst = jsonItem._source.layers.tcp["tcp.dstport"].toInteger()

        time = jsonItem._source.layers.frame["frame.time"]
        time_relative = jsonItem._source.layers.frame["frame.time_relative"].toFloat() * 1000


//        if ("unknown".equals(type) == false) {
//           println line_number + "  " + type
//        }
    }

    boolean isAppData(LazyMap jsonItem) {
        try {
            String type = jsonItem._source.layers.ssl["ssl.record"]["ssl.record.content_type"]
            if ("23".equals(type)) {
                return true
            } else {
                return false
            }
        } catch (Exception) {
            return false
        }
    }

    boolean isNewSessionTicket(LazyMap jsonItem) {
        try {
            String type = jsonItem._source.layers.ssl["ssl.record"]["ssl.handshake"]["ssl.handshake.type"]
            if ("4".equals(type)) {
                return true
            } else {
                return false
            }
        } catch (Exception) {
            return false
        }

    }

    boolean isChangeCipherSpec(LazyMap jsonItem) {
        if ("13".equals(line_number)) {
            int i = 0
        }
        try {
            String type = jsonItem._source.layers.ssl["ssl.record"]["ssl.record.content_type"]
            if ("20".equals(type)) {
                return true
            } else {
                return false
            }
        } catch (Exception) {
            return false
        }
    }

    boolean isClientKeyExchange(LazyMap jsonItem) {
        try {
            String type = jsonItem._source.layers.ssl["ssl.record"]["ssl.handshake"]["ssl.handshake.type"]
            if ("16".equals(type)) {
                return true
            } else {
                return false
            }
        } catch (Exception) {
            return false
        }
    }

    boolean isCertificateStatus(LazyMap jsonItem) {
        try {
            String type = jsonItem._source.layers.ssl["ssl.record"]["ssl.handshake"]["ssl.handshake.cert_status_type"]
            if ("1".equals(type)) {
                return true
            } else {
                return false
            }
        } catch (Exception) {
            return false
        }
    }

    boolean isServerHello(LazyMap jsonItem) {
        try {
            String type = jsonItem._source.layers.ssl["ssl.record"]["ssl.handshake"]["ssl.handshake.type"]
            if ("2".equals(type)) {
                return true
            } else {
                return false
            }
        } catch (Exception) {
            return false
        }
    }

    boolean isCertificate(LazyMap jsonItem) {
        try {
            String type = jsonItem._source.layers.ssl["ssl.record"]["ssl.handshake"]["ssl.handshake.type"]
            if ("11".equals(type)) {
                return true
            } else {
                return false
            }
        } catch (Exception) {
            return false
        }
    }

    boolean isClientHello(LazyMap jsonItem) {
        try {
            String type = jsonItem._source.layers.ssl["ssl.record"]["ssl.handshake"]["ssl.handshake.type"]
            if ("1".equals(type)) {
                return true
            } else {
                return false
            }
        } catch(Exception) {
            return false
        }
    }

    boolean isTcp1(LazyMap jsonItem){
        String flag_syn = jsonItem._source.layers.tcp["tcp.flags_tree"]["tcp.flags.syn"]
        String flag_ack = jsonItem._source.layers.tcp["tcp.flags_tree"]["tcp.flags.ack"]
        String seq = jsonItem._source.layers.tcp["tcp.seq"]

        if ("1".equals(flag_syn)
                && "0".equals(flag_ack)
                && "0".equals(seq)) {
            return true
        } else {
            return false
        }
    }

    boolean isTcp2(LazyMap jsonItem){
        String flag_syn = jsonItem._source.layers.tcp["tcp.flags_tree"]["tcp.flags.syn"]
        String flag_ack = jsonItem._source.layers.tcp["tcp.flags_tree"]["tcp.flags.ack"]
        String seq = jsonItem._source.layers.tcp["tcp.seq"]

        if ("1".equals(flag_syn)
                && "1".equals(flag_ack)
                && "0".equals(seq)) {
            return true
        } else {
            return false
        }
    }

    boolean isTcp3(LazyMap jsonItem){
        String flag_syn = jsonItem._source.layers.tcp["tcp.flags_tree"]["tcp.flags.syn"]
        String flag_ack = jsonItem._source.layers.tcp["tcp.flags_tree"]["tcp.flags.ack"]
        String seq = jsonItem._source.layers.tcp["tcp.seq"]
        String ack = jsonItem._source.layers.tcp["tcp.ack"]
        def ssl = jsonItem._source.layers.ssl
        if ("0".equals(flag_syn)
                && "1".equals(flag_ack)
                && "1".equals(seq)
                && "1".equals(ack)
                && ssl == null
        ) {
            return true
        } else {
            return false
        }
    }

    boolean isOCSPHost() {
        if(Constant.OCSP_HOST.equals(ip_src)
            ||Constant.OCSP_HOST.equals(ip_dst)){
            return true
        } else {
            return false
        }
    }

    boolean sentByServer() {
        if (Constant.LOCAL_IP.equals(ip_dst)) {
            return true
        } else {
            return false
        }

    }
}
