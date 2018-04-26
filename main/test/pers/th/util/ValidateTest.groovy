package pers.th.util

def static hashMapIsNull() {
    def item = new HashMap()
    try {
        Validate.notEmpty(item)
    } catch (IllegalArgumentException ignored) {
        println "hashMapIsNull success"
//        sum = 1
    }
}

hashMapIsNull()
