public class DocumentClassificationAgent {

    def wordsCategorized = [:]

    def addWordCategorization(word, category) {
        wordsCategorized[word] = wordsCategorized[word] ?: [1:0,2:0,3:0,4:0,5:0,6:0,7:0,8:0]
        wordsCategorized[word][category]++
    }

    def registerTrainingData(brTraining) {
        def nofLines = brTraining.readLine().toInteger()
        nofLines.times {
            def pieces = brTraining.readLine().split(" ")
            def category = pieces.head().toInteger()
            def words = pieces.tail()
            words.each { word ->
                if (word.length() >= 4)
                    addWordCategorization(word, category)
            }
        }
        adjustForCategoryTrainingSize()
    }

    private void adjustForCategoryTrainingSize() {
        def wordsPerCategory = [:]
        wordsCategorized.each { word, hitsPerCat ->
            hitsPerCat.each { cat, hits ->
                wordsPerCategory[cat] = wordsPerCategory[cat] ?: 0
                wordsPerCategory[cat] += hits
            }
        }
        wordsCategorized.each { word, hitsPerCat ->
            hitsPerCat.each { cat, hits ->
                wordsCategorized[word][cat] /=
                        (wordsPerCategory[cat] != 0 ? wordsPerCategory[cat] : 1)
            }
        }
    }

    def categorize(String document) {
        def words = document.split(" ").findAll { it.length() >= 4 }
        def categoryScores = (1..8).collect { category ->
            words.sum { word ->
                wordsCategorized[word] != null ? wordsCategorized[word][category] : 0
            }
        }
        categoryScores.indexOf(categoryScores.max()) + 1
    }

    def solveTask(BufferedReader br, BufferedReader brTraining) {
        wordsCategorized = [:]
        registerTrainingData(brTraining)

        def nofDocuments = br.readLine().toInteger()
        nofDocuments.times {
            def line = br.readLine()
            println categorize(line)
        }
    }
}


//BufferedReader br = new BufferedReader(new InputStreamReader(System.in))
//BufferedReader brTraining = new BufferedReader(new FileReader("./trainingdata.txt"))

//new DocumentClassificationAgent().solveTask(br, brTraining)
    