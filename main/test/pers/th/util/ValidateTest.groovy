package pers.th.util

def sum = 0

def static hashMapIsNull() {
    def item = new HashMap()
    try {
        Validate.notEmpty(item)
    } catch (IllegalArgumentException e) {
        println "hashMapIsNull success"
//        sum = 1
    }
}

hashMapIsNull()
