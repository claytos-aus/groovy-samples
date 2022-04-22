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

def dump_simple(n, String s) {
  def s2 = allButLast(s)
  println "$s2=$n"
}

def dump_list(List n, String s) {
  n.eachWithIndex { it, i ->
    if (it instanceof Map) {
      dump_map(it,s)
    } else if (it instanceof List) {
      dump_list(it,s)
    } else {
      def s2 = allButLast(s)
      dump_simple(it,"${s2}[${i}]]")
    }
  }
}

def dump_map(Map n, String s) {
  assert n instanceof Map
  n.each { k,v ->
    s += "$k."
    if (v instanceof Map) {
      dump_map(v, s)
    } else if (v instanceof List) {
      dump_list(v,s)
    } else {
      dump_simple(v,s)
    }
  }
}

// start here
nodes.each { k, v ->
  if (v instanceof Map) {
    dump_map(v, "$k.")
  } else if (v instanceof List) {
    dump_list(v, "$k.")
  } else {
    dump_simple(v, "$k.")
  }
}
