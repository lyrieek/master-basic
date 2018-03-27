package pers.th.util.text

import java.text.SimpleDateFormat

println XStrings.reverse("abcdef")
println XStrings.reverse("12345678")
def randoms = [""]
for (int i = 0; i < 100000; i++)
    randoms.add(UUID.randomUUID().toString())

def start = new Date().getTime()

for (item in randoms)
    println "${item} : " + XStrings.reverse(item)


final SimpleDateFormat format = new SimpleDateFormat("mm:ss SSS")

println format.format(new Date(new Date().getTime() - start))

// done
