#!/usr/bin/env groovy

// read in yaml (stdin), convert to properties format

import groovy.yaml.YamlSlurper

def yaml = new YamlSlurper()
def nodes = yaml.parse(new FileInputStream(FileDescriptor.in))

// strip last char
String allButLast(String s) {
  def t = s.length()-2
  return s[0..t]
}

void dump(Integer n, String s) {
  dump(n.toString(), s)
}
// simple
void dump(def n, String s) {
  def s2 = allButLast(s)
  println "$s2=$n"
}

// list
void dump(List n, String s) {
  n.eachWithIndex { it, i ->
    if (it instanceof Map || it instanceof List) {
      dump(it,s)
    } else {
      def s2 = allButLast(s)
      dump(it,"${s2}[${i}]]")
    }
  }
}

// map
void dump(Map n, String s) {
  n.each { k,v ->
    s += "$k."
    dump(v,s)
  }
}

// start here
nodes.each { k, v ->
  dump(v, "$k.")
}
