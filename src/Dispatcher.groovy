import groovy.json.internal.LazyMap

import java.text.DecimalFormat

/**
 * Created by sodino on 2017/5/22.
 */
class Dispatcher {
    boolean inJobing = false

    ArrayList<NetJob> jobNormalHost = new ArrayList<>(), jobOCSPHost = new ArrayList<>()



    NetJob jobCurrent = new NetJob()



    void dispatch(LazyMap jsonItem) {
        parseNetPackage(jsonItem);
    }


    void parseNetPackage(LazyMap jsonItem) {
        NetPackage pkg = new NetPackage(jsonItem);

        if (jobCurrent.tcp1 == null
                && NetPackage.TCP_1.equals(pkg.type) == false) {
            return
        }

        jobCurrent.add(pkg)

        if (NetPackage.APP_DATA.equals(pkg.type) && pkg.sentByServer()) {
            if (pkg.isOCSPHost()) {
                jobOCSPHost.add(jobCurrent)
//                println "ocsp host add"
            } else {
                jobNormalHost.add(jobCurrent)
//                println "normal host add"
            }

            jobCurrent = null
            jobCurrent = new NetJob()
        }
    }

    def calculate() {
        calculate(jobOCSPHost)
        calculate(jobNormalHost)
    }

    def calculate(ArrayList list) {
        def pattern = "0.000"
        def format = new DecimalFormat(pattern)

        println 'line\ttcp1\t\ttcp2\t\ttcp3\t\tClientHello\tServerHello\tChangeCipher\treq\t\trsp\t\t\tAll\t\t'

        list.forEach{job ->
            print job.tcp1.line_number + '\t\t'
            def t_tcp1 = 0
            print format.format(t_tcp1) + '\t\t'
            def t_tcp2 = job.tcp2.time_relative - job.tcp1.time_relative
            print format.format(t_tcp2) + '\t\t'
            def t_tcp3 = job.tcp3.time_relative - job.tcp2.time_relative
            print format.format(t_tcp3) + '\t\t'
            def t_client_hello = job.clientHello.time_relative - job.tcp3.time_relative
            print format.format(t_client_hello) + '\t\t'
            def t_server_hello = job.serverHello.time_relative - job.clientHello.time_relative
            print format.format(t_server_hello) + '\t\t'


            if (job.certificate != null) {
                def t_certificate = job.certificate.time_relative - job.serverHello.time_relative
                print '[' + format.format(t_certificate) + '|'
            }
            if (job.certificateStatus != null) {
                def t_certificateStatus = job.certificateStatus.time_relative - job.certificate.time_relative
                print format.format(t_certificateStatus) + '|'
            }
            if (job.clientKeyExchange != null) {
                def t_clientKeyExchange = job.clientKeyExchange.time_relative
                            - job.certificateStatus.time_relative
                print format.format(t_clientKeyExchange) + '|'
            }


            def t_tmp = 0
            if (job.newSessionTicket != null) {
                def t_newSessionTicket = job.newSessionTicket.time_relative - job.clientKeyExchange.time_relative
                print format.format(t_newSessionTicket) + ']\t\t'

                t_tmp = job.newSessionTicket.time_relative
            } else {
                def t_changeCipher = job.changeCipherSpec_c.time_relative - job.serverHello.time_relative
                print format.format(t_changeCipher) + '\t\t'

                t_tmp = job.changeCipherSpec_c.time_relative
            }

            if (job.reqAppData != null) {
                def t_reqAppData = job.reqAppData.time_relative - t_tmp
                print format.format(t_reqAppData) + '\t\t'

                def t_rspAppData = job.rspAppData.time_relative - job.reqAppData.time_relative
                print format.format(t_rspAppData) + '\t\t'
            } else {
                print 'none \t\t'
                print 'none \t\t'
            }
            def t_rsp = job.rspAppData.time_relative - job.tcp1.time_relative
            print format.format(t_rsp) + '\t\t'

            println ""
        }
    }
}
