#!/usr/bin/env groovy

// read in yaml (stdin), convert to properties format

import groovy.yaml.YamlSlurper

def yaml = new YamlSlurper()
def nodes = yaml.parse(new FileInputStream(FileDescriptor.in))

// strip last char
def allButLast(String s) {
  def t = s.length()-2
  return s[0..t]
}

// simple
def dump(n, String s) {
  def s2 = allButLast(s)
  println "$s2=$n"
}

// list
def dump(List n, String s) {
  n.eachWithIndex { it, i ->
    if (it instanceof Map) {
      dump(it,s)
    } else if (it instanceof List) {
      dump(it,s)
    } else {
      def s2 = allButLast(s)
      dump(it,"${s2}[${i}]]")
    }
  }
}

// map
def dump(Map n, String s) {
  assert n instanceof Map
  n.each { k,v ->
    s += "$k."
    if (v instanceof Map) {
      dump(v, s)
    } else if (v instanceof List) {
      dump(v,s)
    } else {
      dump(v,s)
    }
  }
}

// start here
nodes.each { k, v ->
  if (v instanceof Map) {
    dump(v, "$k.")
  } else if (v instanceof List) {
    dump(v, "$k.")
  } else {
    dump(v, "$k.")
  }
}
