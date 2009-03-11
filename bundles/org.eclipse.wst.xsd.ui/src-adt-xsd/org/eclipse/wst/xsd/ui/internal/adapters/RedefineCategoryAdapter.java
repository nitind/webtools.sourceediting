/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xsd.ui.internal.adapters;


import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xsd.ui.internal.adt.actions.BaseSelectionAction;
import org.eclipse.wst.xsd.ui.internal.adt.actions.ShowPropertiesViewAction;
import org.eclipse.wst.xsd.ui.internal.adt.facade.IModel;
import org.eclipse.wst.xsd.ui.internal.common.actions.AddXSDRedefinableContentAction;
import org.eclipse.xsd.XSDRedefine;


public class RedefineCategoryAdapter extends CategoryAdapter
{
  protected XSDRedefine xsdRedefine;

  public RedefineCategoryAdapter(String label, Image image, Collection children, XSDRedefine xsdRedefine, int groupType)
  {
    super(label, image, children, xsdRedefine.getSchema(), groupType);
    this.xsdRedefine = xsdRedefine;
    this.target = xsdRedefine;
  }

  public XSDRedefine getXSDRedefine()
  {
    return xsdRedefine;
  }

  public String[] getActions(Object object)
  {
    Collection actionIDs = new ArrayList();

    switch (groupType)
    {
      case TYPES:
      {
        actionIDs.add(AddXSDRedefinableContentAction.COMPLEX_TYPE_ID);
        actionIDs.add(AddXSDRedefinableContentAction.SIMPLE_TYPE_ID);
        break;
      }
      case GROUPS:
      {
        actionIDs.add(AddXSDRedefinableContentAction.MODEL_GROUP_ID);
        break;
      }
      case ATTRIBUTES:
      {
        actionIDs.add(AddXSDRedefinableContentAction.ATTRIBUTE_GROUP_ID);
        break;
      }
      case ATTRIBUTE_GROUPS:
      {
        actionIDs.add(AddXSDRedefinableContentAction.ATTRIBUTE_GROUP_ID);
        break;
      }
    }
    actionIDs.add(BaseSelectionAction.SEPARATOR_ID);
    actionIDs.add(ShowPropertiesViewAction.ID);
    return (String[])actionIDs.toArray(new String [0]);
  }

  public IModel getModel()
  {
    return (IModel)XSDAdapterFactory.getInstance().adapt(xsdRedefine.getSchema());
  }
}