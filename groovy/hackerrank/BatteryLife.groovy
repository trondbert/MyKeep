
void estimate() {
    BufferedReader brLog = new BufferedReader(new FileReader("./trainingdata.txt"))
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in))

    def logLines = (1..100).collect {
        brLog.readLine().split(",").collect { it.toDouble() }
    }
    def countValid = 0
    def sumRatios = logLines.sum {
        if (it[0].intValue() != 0) {
            countValid++
            def ratioUsableTimeToChargeTime = it[1] / it[0]
            return ratioUsableTimeToChargeTime
        }
        return 0.0
    }

    def chargeTimeNow = br.readLine().toDouble()
    println (chargeTimeNow * sumRatios) / countValid
}

estimate()
