/*
* Copyright (c) 2002 IBM Corporation and others.
* All rights reserved.   This program and the accompanying materials
* are made available under the terms of the Common Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/cpl-v10.html
* 
* Contributors:
*   IBM - Initial API and implementation
*   Jens Lukowski/Innoopract - initial renaming/restructuring
* 
*/
package org.eclipse.wst.xml.ui.ui.dialogs;

import java.text.Collator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImageHelper;
import org.eclipse.wst.xml.ui.internal.editor.XMLEditorPluginImages;
import org.eclipse.wst.xml.uriresolver.XMLCatalogEntry;
import org.eclipse.wst.xml.uriresolver.util.URIHelper;


public class XMLCatalogTableViewer extends TableViewer {

	protected static Image unknownFileImage = XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_OBJ_TXTEXT);
	protected static Image dtdFileImage = XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_OBJ_DTDFILE);
	protected static Image xsdFileImage = XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_OBJ_XSDFILE);
	protected static Image errorImage = XMLEditorPluginImageHelper.getInstance().getImage(XMLEditorPluginImages.IMG_OVR_ERROR);

	protected static String ERROR_STATE_KEY = "errorstatekey"; //$NON-NLS-1$

	//protected ImageFactory imageFactory = new ImageFactory();   

	public XMLCatalogTableViewer(Composite parent, String[] columnProperties) {
		super(parent, SWT.FULL_SELECTION);

		Table table = getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableLayout layout = new TableLayout();
		for (int i = 0; i < columnProperties.length; i++) {
			TableColumn column = new TableColumn(table, i);
			column.setText(columnProperties[i]);
			column.setAlignment(SWT.LEFT);
			layout.addColumnData(new ColumnWeightData(50, true));
		}
		table.setLayout(layout);
		table.setLinesVisible(false);

		setColumnProperties(columnProperties);

		setContentProvider(new CatalogEntryContentProvider());
		setLabelProvider(new CatalogEntryLabelProvider());
	}

	public void setFilterExtensions(String[] extensions) {
		resetFilters();
		addFilter(new XMLCatalogTableViewerFilter(extensions));
	}

	public Collection getXMLCatalogEntries() {
		return null;
	}

	public class CatalogEntryLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object object, int columnIndex) {
			String result = null;
			if (object instanceof XMLCatalogEntry) {
				XMLCatalogEntry catalogEntry = (XMLCatalogEntry) object;
				result = columnIndex == 0 ? catalogEntry.getKey() : catalogEntry.getURI();
				result = URIHelper.removePlatformResourceProtocol(result);
			}
			return result != null ? result : ""; //$NON-NLS-1$
		}

		public Image getColumnImage(Object object, int columnIndex) {
			Image result = null;
			if (columnIndex == 0) {
				Image base = null;
				if (object instanceof XMLCatalogEntry) {
					XMLCatalogEntry catalogEntry = (XMLCatalogEntry) object;
					String uri = catalogEntry.getURI();
					if (uri.endsWith("dtd")) { //$NON-NLS-1$
						base = dtdFileImage;
					}
					else if (uri.endsWith("xsd")) { //$NON-NLS-1$
						base = xsdFileImage;
					}
					else {
						base = unknownFileImage;
					}

					if (base != null) {
						if (URIHelper.isReadableURI(uri, false)) {
							result = base;
						}
						else {
							//TODO... SSE port 	    
							result = base;//imageFactory.createCompositeImage(base, errorImage, ImageFactory.BOTTOM_LEFT);
						}
					}
				}
			}
			return result;
		}
	}


	public class CatalogEntryContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object element) {
			Object[] array = getXMLCatalogEntries().toArray();
			Comparator comparator = new Comparator() {
				public int compare(Object o1, Object o2) {
					int result = 0;
					if (o1 instanceof XMLCatalogEntry && o2 instanceof XMLCatalogEntry) {
						XMLCatalogEntry mappingInfo1 = (XMLCatalogEntry) o1;
						XMLCatalogEntry mappingInfo2 = (XMLCatalogEntry) o2;
						result = Collator.getInstance().compare(mappingInfo1.getKey(), mappingInfo2.getKey());
					}
					return result;
				}
			};
			Arrays.sort(array, comparator);
			return array;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object old, Object newobj) {
		}

		public boolean isDeleted(Object object) {
			return false;
		}
	}


	public void menuAboutToShow(IMenuManager menuManager) {
		Action action = new Action("hello") { //$NON-NLS-1$
			public void run() {
				System.out.println("run!"); //$NON-NLS-1$
			}
		};
		menuManager.add(action);
	}


	class XMLCatalogTableViewerFilter extends ViewerFilter {
		protected String[] extensions;

		public XMLCatalogTableViewerFilter(String[] extensions) {
			this.extensions = extensions;
		}

		public boolean isFilterProperty(Object element, Object property) {
			return false;
		}

		public boolean select(Viewer viewer, Object parent, Object element) {
			boolean result = false;
			if (element instanceof XMLCatalogEntry) {
				XMLCatalogEntry catalogEntry = (XMLCatalogEntry) element;
				for (int i = 0; i < extensions.length; i++) {
					if (catalogEntry.getURI().endsWith(extensions[i])) {
						result = true;
						break;
					}
				}
			}
			return result;
		}
	}
}
