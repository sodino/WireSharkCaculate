/**
 * Created by sodino on 2017/5/26.
 */
class SumTime {
    def mapSum = [:]
    def mapCount = [:] // 有时候会出现抓包抓不到的情况，会导致计数与request的次数不同

    def add(String step, double time) {
        addTime(step, time)
        addCount(step)
    }

    def addTime(String step, double time) {
        def result = mapSum[step];
        if (result == null) {
            mapSum.put(step, time)
        } else {
            double sum = result
            sum += time
            mapSum.put(step, sum)
        }
    }

    def addCount(String step) {
        def result = mapCount[step];
        if (result == null) {
            mapCount.put(step, 1)
        } else {
            int count = result
            count ++
            mapCount.put(step, count)
        }
    }

    def printAverage(ArrayList<String> arrStep) {
        def format = Constant.Data_Format

        println '----- ----- ----- ----- ----- ----- -----'
        print 'avg\t\t0.000\t'

        arrStep.each {step->
            if (Constant.Handshake.TCP_1.equals(step)) {
                return
            }

            def sum = mapSum[step]
            def count = mapCount[step]
            if (sum == null || count == null || count == 0) {
                print 'x.xxx\t'
            } else {
                def value = format.format(sum / count)
                print value + '\t'
            }
        }
        println ''
    }
}
