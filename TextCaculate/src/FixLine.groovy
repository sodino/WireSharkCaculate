/**
 * Created by sodino on 2017/5/26.
 */
class FixLine {
    def static spliteLine(String[] listTitle, String line) {
        def map = [:]
        int count = 0
        def info = ''
        def arr = line.split(' +')
        arr.each {value ->
            if (value.length() == 0) {
                return
            }
            count ++

            if (count < listTitle.length) {
                def key = listTitle[count -1]
                if (Constant.NO.equals(key)) {
                    value = value.toInteger()
                } else if (Constant.TIME.equals(key)) {
                    value = value.toFloat()
                }
                map.put(key, value)
            } else {
                info += value + ' '
                count --
            }
        }
        info = info.trim()
        if (info.length() > 0) {
            map.put(listTitle[count], info)
        }

        map
    }
}
