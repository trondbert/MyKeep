//evaluate(new File("./DocumentClassificationAgent.groovy"))

def input = ["2", "many freedom fighters grow weary", "ain't no grub on the plate ma"].reverse() as Stack
def training = ["3", "1 freedom figthers seldom win in the long run ma",
                    "2 where's the grub ma", "2 ma and pa cleaned the plate"].reverse() as Stack


BufferedReader br = new BufferedReader(new InputStreamReader(System.in)) {
    @Override
    public String readLine() {
        input.size() > 0 ? input.pop() : null
    }
}
BufferedReader brTraining = new BufferedReader(new FileReader("./trainingdata.txt")) {
    //@Override
    //public String readLine() throws IOException {
    //    training.size() > 0 ? training.pop() : null
    //}
}
br = new BufferedReader(new FileReader("./inputDocumentClassification.txt"))

new DocumentClassificationAgent().solveTask(br, brTraining)
