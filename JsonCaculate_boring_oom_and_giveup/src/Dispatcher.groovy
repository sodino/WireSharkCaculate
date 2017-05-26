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
        if (list.size() == 0) {
            return
        }
        def pattern = "0.000"
        def format = new DecimalFormat(pattern)

        println 'line\ttcp1\t\ttcp2\t\ttcp3\t\tClientHello\tServerHello\tChangeCipher\treq\t\trsp\t\t\tAll\t\t'

        float sumTcp2 = 0
        float sumTcp3 = 0
        float sumClientHello = 0
        float sumServerHello = 0
        float sumChangeCipherSpec = 0
        float sumReqAppData = 0
        float sumRspAppData = 0
        float sum = 0

        list.forEach{job ->
            print job.tcp1.line_number + '\t\t'
            def t_tcp1 = 0
            print format.format(t_tcp1) + '\t\t'

            def t_tcp2 = job.tcp2.time_relative - job.tcp1.time_relative
            sumTcp2 += t_tcp2
            print format.format(t_tcp2) + '\t\t'

            def t_tcp3 = job.tcp3.time_relative - job.tcp2.time_relative
            sumTcp3 += t_tcp3
            print format.format(t_tcp3) + '\t\t'

            def t_client_hello = job.clientHello.time_relative - job.tcp3.time_relative
            sumClientHello += t_client_hello
            print format.format(t_client_hello) + '\t\t'

            def t_server_hello = job.serverHello.time_relative - job.clientHello.time_relative
            sumServerHello += t_server_hello
            print format.format(t_server_hello) + '\t\t'


            if (job.certificate != null) {
                def t_certificate = job.certificate.time_relative - job.serverHello.time_relative
                sumChangeCipherSpec += t_certificate
                print '[' + format.format(t_certificate) + '|'
            }
            if (job.certificateStatus != null) {
                def t_certificateStatus = job.certificateStatus.time_relative - job.certificate.time_relative
                sumChangeCipherSpec += t_certificateStatus
                print format.format(t_certificateStatus) + '|'
            }
            if (job.clientKeyExchange != null) {
                def tmpPkg = job.certificateStatus != null ? job.certificateStatus : job.certificate
                def t_clientKeyExchange = job.clientKeyExchange.time_relative - tmpPkg.time_relative
                sumChangeCipherSpec += t_clientKeyExchange
                print format.format(t_clientKeyExchange) + '|'
            } else {
                print '[  \tnone\t  ]'
            }


            def t_tmp = 0

            def tmpPkg = job.newSessionTicket != null ? job.netSessionTicket : job.changeCipherSpec_s
            if (tmpPkg != null) {
                // 有开启 Session Resumption的话
                def t_newSessionTicket = tmpPkg.time_relative - job.clientKeyExchange.time_relative
                sumChangeCipherSpec += t_newSessionTicket
                print format.format(t_newSessionTicket) + ']\t\t'

                t_tmp = tmpPkg.time_relative
            } else {
                def t_changeCipher = job.changeCipherSpec_c.time_relative - job.serverHello.time_relative
                sumChangeCipherSpec += t_changeCipher
                print format.format(t_changeCipher) + '\t\t'

                t_tmp = job.changeCipherSpec_c.time_relative
            }

            if (job.reqAppData != null) {
                def t_reqAppData = job.reqAppData.time_relative - t_tmp
                sumReqAppData += t_reqAppData
                print format.format(t_reqAppData) + '\t\t'

                def t_rspAppData = job.rspAppData.time_relative - job.reqAppData.time_relative
                sumRspAppData += t_rspAppData
                print format.format(t_rspAppData) + '\t\t'
            } else {
                print 'none \t\t'
                print 'none \t\t'
            }
            def t_rsp = job.rspAppData.time_relative - job.tcp1.time_relative
            sum += t_rsp
            print format.format(t_rsp) + '\t\t'

            println ""
        }


        int size = list.size()
        def avgTcp2 = sumTcp2 / size
        def avgTcp3 = sumTcp3 / size
        def avgClientHello = sumClientHello / size
        def avgServerHello = sumServerHello / size
        def avgChangeCipherSpec = sumChangeCipherSpec / size
        def avgReq = sumReqAppData / size
        def avgRsp = sumRspAppData / size
        def avg = sum /size

        println '------ ------ ------ ------ ------ ------ ------ ------'
        print 'Avg\t\t0.000\t\t' + format.format(avgTcp2) + '\t\t' + format.format(avgTcp3) + '\t\t'
        print format.format(avgClientHello) + '\t\t' + format.format(avgServerHello) + '\t\t'
        print format.format(avgChangeCipherSpec) + '\t\t'
        print format.format(avgReq) + '\t\t'
        print format.format(avgRsp) + '\t\t'
        print format.format(avg) + '\t\t'
        println ""

    }
}
