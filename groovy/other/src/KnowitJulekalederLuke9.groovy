import java.io.*

BufferedReader br = new BufferedReader(
    	new InputStreamReader(new URL("http://pastebin.com/raw/2vstb018").openStream()));
        /*new StringReader("None,a,30\n" +
                "a,b,15\n" +
                "b,c,12\n"));
*/


def moneyMap = [:]
def i = 0
def line = null
while ((line = br.readLine()) != null) {
    def trans = line.split(",")
    println i + " " + trans
    def receiver = trans[1]
    def sender = trans[0]
    def moneys = Integer.valueOf(trans[2])
    if (!sender.equals("None")) {
        moneyMap.put(sender, moneyMap.get(sender, 0) - moneys)
    }
    moneyMap.put(receiver, moneyMap.get(receiver, 0) + moneys)
    i++
}

moneyMap.each { k,v -> println "$k: $v" }
println(moneyMap.findAll { it.value > 10 }.size())