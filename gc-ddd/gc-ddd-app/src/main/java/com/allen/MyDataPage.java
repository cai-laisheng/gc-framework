package com.allen;

import wicket.Component;
import wicket.markup.html.basic.Label;
import wicket.model.AbstractReadOnlyModel;
import net.databinder.components.DataPage;

public class MyDataPage extends DataPage {
	
	/**
	 * @return name appears in page title, and often within the page
	 */
	@Override
	protected String getName() {
		return "gc-ddd-app";
	}
	
	/**
	 * Add components to page.
	 */
	public MyDataPage() {
		super();
		
		// if getName() is invariant, simplify to Model(getName())
		add(new Label("pageTitleHeader", new AbstractReadOnlyModel() {
			@Override
			public Object getObject(Component component) {
				return getName();
			}
		}));	
	}
}
