/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Anton Leherbauer (Wind River Systems)
 *******************************************************************************/

package org.eclipse.cdt.internal.ui.preferences.formatter;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.BackingStoreException;

import org.eclipse.cdt.ui.CUIPlugin;


public class FormatterProfileStore extends ProfileStore {

	/**
	 * Preference key where all profiles are stored
	 */
	private static final String PREF_FORMATTER_PROFILES= "org.eclipse.cdt.ui.formatterprofiles"; //$NON-NLS-1$
	private static final String PREF_FORMATTER_PROFILES_OLD= "org.eclipse.jdt.ui.formatterprofiles"; //$NON-NLS-1$
	
	public FormatterProfileStore(IProfileVersioner profileVersioner) {
		super(PREF_FORMATTER_PROFILES, profileVersioner);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List readProfiles(IScopeContext scope) throws CoreException {
		final IEclipsePreferences node= scope.getNode(CUIPlugin.PLUGIN_ID);
		final String profilesValue= node.get(PREF_FORMATTER_PROFILES_OLD, null);
		if (profilesValue != null) {
			// migrate to new preference key
			final String versionKeyOld = PREF_FORMATTER_PROFILES_OLD + VERSION_KEY_SUFFIX;
			String version= node.get(versionKeyOld, null);
			node.put(PREF_FORMATTER_PROFILES, profilesValue);
			node.put(PREF_FORMATTER_PROFILES + VERSION_KEY_SUFFIX, version);
			node.remove(PREF_FORMATTER_PROFILES_OLD);
			node.remove(versionKeyOld);
			try {
				node.flush();
			} catch (BackingStoreException exc) {
				return readProfilesFromString(profilesValue);
			}
		}
	    return super.readProfiles(scope);
	}

}
