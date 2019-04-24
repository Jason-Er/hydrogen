package com.thumbstage.hydrogen.utils;

import com.thumbstage.hydrogen.model.bo.User;

import java.util.Comparator;

public class PinyinComparator implements Comparator<User> {

	public int compare(User o1, User o2) {
		if (PinyinUtils.getFirstSpell(o1.getName()).equals("@")
				|| PinyinUtils.getFirstSpell(o2.getName()).equals("#")) {
			return 1;
		} else if (PinyinUtils.getFirstSpell(o1.getName()).equals("#")
				|| PinyinUtils.getFirstSpell(o2.getName()).equals("@")) {
			return -1;
		} else {
			return o1.getName().compareTo(o2.getName());
		}
	}

}
