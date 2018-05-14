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

import org.sleuthkit.datamodel.TimelineManager;
import org.sleuthkit.datamodel.TskData;

/**
 * Filter to hide known files
 */
final public class HideKnownFilter extends AbstractFilter {

	@Override
	public String getDisplayName() {
		return BundleUtils.getBundle().getString("hideKnownFilter.displayName.text");
	}

	public HideKnownFilter() {
		super();
		setSelected(false);
	}

	@Override
	public HideKnownFilter copyOf() {
		HideKnownFilter hideKnownFilter = new HideKnownFilter();
		hideKnownFilter.setSelected(isSelected());
		hideKnownFilter.setDisabled(isDisabled());
		return hideKnownFilter;
	}

	@Override
	public int hashCode() {
		return 7;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final HideKnownFilter other = (HideKnownFilter) obj;

		return isSelected() == other.isSelected();
	}

	@Override
	public String getSQLWhere(TimelineManager manager) {
		return this.isActive()
				? "(known_state != " + TskData.FileKnown.KNOWN.getFileKnownValue() + ")" // NON-NLS
				: manager.getTrueLiteral();
	}
}
