/**
 * Created by sodino on 2017/5/23.
 */
class FixLine {

    def tagStartLayers = '"layers":'
    def tagStartSSL = '"ssl":'
    def tagStartSSLRecord = '"ssl.record":'
    int countSSL = 0
    int countSSLrecord = 0


    String fixSSL(String line){
        if (line.contains(tagStartLayers)) {
            countSSL = 0
        }
        if (line.contains(tagStartSSL)) {
            if (countSSL > 0) {
                line = line.replace(tagStartSSL, '"ssl_' + countSSL + '":')
            }

            countSSL ++
        }

        return line
    }

    String fixSSLRecord(String line){
        if (line.contains(tagStartSSL)) {
            countSSLrecord = 0
        }
        if (line.contains(tagStartSSLRecord)) {
            if (countSSLrecord > 0) {
                // 避免json解析时多个ssl.record被覆盖成只剩下最后出现的
                line = line.replace(tagStartSSLRecord, '"ssl.record_' + countSSLrecord + '":')
            }

            countSSLrecord ++
        }

        return line
    }

}
