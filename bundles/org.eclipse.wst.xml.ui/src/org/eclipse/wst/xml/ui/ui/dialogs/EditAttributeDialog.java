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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.wst.xml.ui.nls.ResourceHandler;
import org.eclipse.wst.xml.ui.ui.XMLCommonResources;
import org.eclipse.wst.xml.ui.ui.XMLCommonUIContextIds;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;



public class EditAttributeDialog extends Dialog implements ModifyListener {
	protected Element ownerElement;
	protected Attr attribute;
	protected Text attributeNameField;
	protected Text attributeValueField;
	protected Button okButton;
	protected String attributeName;
	protected String attributeValue;
	protected Label errorMessageLabel;

	public EditAttributeDialog(Shell parentShell, Element ownerElement, Attr attribute) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		this.ownerElement = ownerElement;
		this.attribute = attribute;
	}

	protected Control createContents(Composite parent) {
		Control control = super.createContents(parent);
		attributeNameField.forceFocus();
		attributeNameField.selectAll();
		updateErrorMessage();
		return control;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}


	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);
		WorkbenchHelp.setHelp(dialogArea, XMLCommonUIContextIds.XCUI_ATTRIBUTE_DIALOG);

		Composite composite = new Composite(dialogArea, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		composite.setLayout(layout);

		//
		// Style convenience constants  
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label attributeNameLabel = new Label(composite, SWT.NONE);
		attributeNameLabel.setText(XMLCommonResources.getInstance().getString("_UI_LABEL_NAME_COLON")); //$NON-NLS-1$

		attributeNameField = new Text(composite, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		attributeNameField.setLayoutData(gd);
		attributeNameField.setText(getDisplayValue(attribute != null ? attribute.getName() : "")); //$NON-NLS-1$
		attributeNameField.addModifyListener(this);

		Label attributeValueLabel = new Label(composite, SWT.NONE);
		attributeValueLabel.setText(XMLCommonResources.getInstance().getString("_UI_LABEL_VALUE_COLON")); //$NON-NLS-1$

		String value = attribute != null ? attribute.getValue() : ""; //$NON-NLS-1$
		int style = SWT.SINGLE | SWT.BORDER;
		if (value.indexOf("\n") != -1) { //$NON-NLS-1$
			style = SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL;
		}

		attributeValueField = new Text(composite, style);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		attributeValueField.setLayoutData(gd);
		attributeValueField.setText(getDisplayValue(attribute != null ? attribute.getValue() : "")); //$NON-NLS-1$

		// error message
		errorMessageLabel = new Label(composite, SWT.WRAP);
		errorMessageLabel.setText(ResourceHandler.getString("error_message_goes_here")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 200;
		gd.heightHint = Math.max(30, errorMessageLabel.computeSize(0, 0, false).y * 2);
		gd.horizontalSpan = 2;
		errorMessageLabel.setLayoutData(gd);
		Color color = new Color(errorMessageLabel.getDisplay(), 200, 0, 0);
		errorMessageLabel.setForeground(color);

		return dialogArea;
	}

	public void modifyText(ModifyEvent e) {
		updateErrorMessage();
	}

	protected void updateErrorMessage() {
		String errorMessage = null;
		String name = attributeNameField.getText().trim();
		if (name.length() > 0) {
			Attr matchingAttribute = ownerElement.getAttributeNode(name);
			if (matchingAttribute != null && matchingAttribute != attribute) {
				errorMessage = XMLCommonResources.getInstance().getString("_ERROR_XML_ATTRIBUTE_ALREADY_EXISTS"); //$NON-NLS-1$
			}
			else {
				// TODO get checkName from Model
				//errorMessage = ValidateHelper.checkXMLName(name);
			}
		}
		else {
			errorMessage = ""; //$NON-NLS-1$
		}

		errorMessageLabel.setText(errorMessage != null ? errorMessage : ""); //$NON-NLS-1$
		errorMessageLabel.getParent().layout();
		okButton.setEnabled(errorMessage == null);
	}

	protected String getDisplayValue(String string) {
		return string != null ? string : ""; //$NON-NLS-1$
	}

	protected String getModelValue(String string) {
		String result = null;
		if (string != null && string.trim().length() > 0) {
			result = string;
		}
		return result;
	}

	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			attributeName = getModelValue(attributeNameField.getText());
			attributeValue = attributeValueField.getText();
		}
		super.buttonPressed(buttonId);
	}

	public String getAttributeName() {
		return attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}
}
