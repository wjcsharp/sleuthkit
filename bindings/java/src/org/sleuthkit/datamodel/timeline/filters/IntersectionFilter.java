/*
 * Sleuth Kit Data Model
 *
 * Copyright 2018 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.datamodel.timeline.filters;

import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import org.apache.commons.lang3.StringUtils;
import org.sleuthkit.datamodel.TimelineManager;

/**
 * Intersection (And) filter
 *
 * @param <S> The type of sub Filters in this IntersectionFilter.
 */
public class IntersectionFilter<S extends Filter> extends CompoundFilter<S> {

	public IntersectionFilter(List<S> subFilters) {
		super(subFilters);
	}

	public IntersectionFilter() {
		super(FXCollections.<S>observableArrayList());
	}

	@Override
	public IntersectionFilter<S> copyOf() {
		@SuppressWarnings("unchecked")
		IntersectionFilter<S> filter = new IntersectionFilter<>(
				(List<S>) this.getSubFilters().stream()
						.map(Filter::copyOf)
						.collect(Collectors.toList()));
		filter.setSelected(isSelected());
		filter.setDisabled(isDisabled());
		return filter;
	}

	@Override
	public String getDisplayName() {
		String collect = getSubFilters().stream()
				.map(Filter::getDisplayName)
				.collect(Collectors.joining(",", "[", "]"));
		return BundleUtils.getBundle().getString("IntersectionFilter.displayName.text") + collect;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		@SuppressWarnings("unchecked")
		final IntersectionFilter<S> other = (IntersectionFilter<S>) obj;

		if (isSelected() != other.isSelected()) {
			return false;
		}

		for (int i = 0; i < getSubFilters().size(); i++) {
			if (getSubFilters().get(i).equals(other.getSubFilters().get(i)) == false) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String getSQLWhere(TimelineManager manager) {
		String query = this.getSubFilters().stream()
				.filter(Filter::isActive)
				.map(filter -> filter.getSQLWhere(manager))
				.collect(Collectors.joining(" AND "));
		return "(" + StringUtils.defaultIfBlank(query, manager.getTrueLiteral()) + ")";
	}
}
