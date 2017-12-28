package com.helmo.archi.google.googleuse.service;

import java.util.List;
import java.util.stream.Collectors;

public interface AccessRange<T, IdType> extends BasicService<T, IdType> {
	default List<T> getRange(long one, long two) {
		return getAll()
			  .stream()
			  .skip(one)
			  .limit(two - one)
			  .collect(Collectors.toList());
	}
}
