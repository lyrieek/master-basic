package pers.th.util

def s = new String[2]
s[0] = "foo"
s[1] = "bar"

SList<String> list = new SList(s)

println list.get(0)