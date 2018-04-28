package pers.th.util.math

def ran = new IRandom()
println ran.nextInt(100)

ran = new IRandom(10)
println ran.nextInt(100)