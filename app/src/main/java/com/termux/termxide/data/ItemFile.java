package com.termux.termxide.data;

import java.util.Date;

enum Type {
	C, Cpp, Rust, Java, JS, Html, Css
}

public class ItemFile {
	String name;
	long size;
	Type type;
	Date creation;
}
