/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.markup.html.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.version.undo.Change;


/**
 * Abstract base class for all choice (html select) options.
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 * @author Johan Compagner
 */
abstract class AbstractChoice extends FormComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The list of objects. */
	private IModel choices;

	/** The renderer used to generate display/id values for the objects. */
	private IChoiceRenderer renderer;

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @see org.apache.wicket.Component#Component(String)
	 */
	public AbstractChoice(final String id)
	{
		this(id, new Model(new ArrayList()), new ChoiceRenderer());
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param choices
	 *            The collection of choices in the dropdown
	 * @see org.apache.wicket.Component#Component(String)
	 */
	public AbstractChoice(final String id, final List choices)
	{
		this(id, new Model((Serializable)choices), new ChoiceRenderer());
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param renderer
	 *            The rendering engine
	 * @param choices
	 *            The collection of choices in the dropdown
	 * @see org.apache.wicket.Component#Component(String)
	 */
	public AbstractChoice(final String id, final List choices, final IChoiceRenderer renderer)
	{
		this(id, new Model((Serializable)choices), renderer);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The collection of choices in the dropdown
	 * @see org.apache.wicket.Component#Component(String, IModel)
	 */
	public AbstractChoice(final String id, IModel model, final List choices)
	{
		this(id, model, new Model((Serializable)choices), new ChoiceRenderer());
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The drop down choices
	 * @param renderer
	 *            The rendering engine
	 * @see org.apache.wicket.Component#Component(String, IModel)
	 */
	public AbstractChoice(final String id, IModel model, final List choices,
			final IChoiceRenderer renderer)
	{
		this(id, model, new Model((Serializable)choices), renderer);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param choices
	 *            The collection of choices in the dropdown
	 * @see org.apache.wicket.Component#Component(String)
	 */
	public AbstractChoice(final String id, final IModel choices)
	{
		this(id, choices, new ChoiceRenderer());
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param renderer
	 *            The rendering engine
	 * @param choices
	 *            The collection of choices in the dropdown
	 * @see org.apache.wicket.Component#Component(String)
	 */
	public AbstractChoice(final String id, final IModel choices, final IChoiceRenderer renderer)
	{
		super(id);
		this.choices = wrap(choices);
		setChoiceRenderer(renderer);
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param choices
	 *            The collection of choices in the dropdown
	 * @see org.apache.wicket.Component#Component(String, IModel)
	 */
	public AbstractChoice(final String id, IModel model, final IModel choices)
	{
		this(id, model, choices, new ChoiceRenderer());
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param model
	 *            See Component
	 * @param renderer
	 *            The rendering engine
	 * @param choices
	 *            The drop down choices
	 * @see org.apache.wicket.Component#Component(String, IModel)
	 */
	public AbstractChoice(final String id, IModel model, final IModel choices,
			final IChoiceRenderer renderer)
	{
		super(id, model);
		this.choices = wrap(choices);
		setChoiceRenderer(renderer);
	}


	/**
	 * @return The collection of object that this choice has
	 */
	public List getChoices()
	{
		List choices = (this.choices != null) ? (List)this.choices.getObject() : null;
		if (choices == null)
		{
			throw new NullPointerException(
					"List of choices is null - Was the supplied 'Choices' model empty?");
		}
		return choices;
	}


	/**
	 * Sets the list of choices
	 * 
	 * @param choices
	 *            model representing the list of choices
	 * @return this for chaining
	 */
	public final AbstractChoice setChoices(IModel choices)
	{
		if (this.choices != null && this.choices != choices)
		{
			if (isVersioned())
			{
				addStateChange(new ChoicesListChange());
			}
		}
		this.choices = wrap(choices);
		return this;
	}

	/**
	 * Sets the list of choices.
	 * 
	 * @param choices
	 *            the list of choices
	 * @return this for chaining
	 */
	public final AbstractChoice setChoices(List choices)
	{
		if ((this.choices != null))
		{
			if (isVersioned())
			{
				addStateChange(new ChoicesListChange());
			}
		}
		this.choices = new Model((Serializable)choices);
		return this;
	}

	/**
	 * @return The IChoiceRenderer used for rendering the data objects
	 */
	public final IChoiceRenderer getChoiceRenderer()
	{
		return renderer;
	}

	/**
	 * Set the choice renderer to be used.
	 * 
	 * @param renderer
	 * @return this for chaining
	 */
	public final AbstractChoice setChoiceRenderer(IChoiceRenderer renderer)
	{
		if (renderer == null)
		{
			renderer = new ChoiceRenderer();
		}
		this.renderer = renderer;
		return this;
	}

	/**
	 * @see org.apache.wicket.Component#detachModel()
	 */
	protected void detachModel()
	{
		super.detachModel();

		if (choices != null)
		{
			choices.detach();
		}
	}

	/**
	 * 
	 * @param selected
	 *            The object that's currently selected
	 * @return Any default choice, such as "Choose One", depending on the subclass
	 */
	protected CharSequence getDefaultChoice(final Object selected)
	{
		return "";
	}

	/**
	 * Gets whether the given value represents the current selection.
	 * 
	 * @param object
	 *            The object to check
	 * @param index
	 *            The index in the choices collection this object is in.
	 * @param selected
	 *            The currently selected string value
	 * @return Whether the given value represents the current selection
	 */
	protected abstract boolean isSelected(final Object object, int index, String selected);

	/**
	 * Gets whether the given value is disabled. This default implementation always returns false.
	 * 
	 * @param object
	 *            The object to check
	 * @param index
	 *            The index in the choices collection this object is in.
	 * @param selected
	 *            The currently selected string value
	 * @return Whether the given value represents the current selection
	 */
	protected boolean isDisabled(final Object object, int index, String selected)
	{
		return false;
	}

	/**
	 * Handle the container's body.
	 * 
	 * @param markupStream
	 *            The markup stream
	 * @param openTag
	 *            The open tag for the body
	 * @see org.apache.wicket.Component#onComponentTagBody(MarkupStream, ComponentTag)
	 */
	protected void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
	{
		List choices = getChoices();
		final AppendingStringBuffer buffer = new AppendingStringBuffer((choices.size() * 50) + 16);
		final String selected = getValue();

		// Append default option
		buffer.append(getDefaultChoice(selected));

		for (int index = 0; index < choices.size(); index++)
		{
			final Object choice = choices.get(index);
			appendOptionHtml(buffer, choice, index, selected);
		}

		buffer.append("\n");
		replaceComponentTagBody(markupStream, openTag, buffer);
	}

	/**
	 * Generates and appends html for a single choice into the provided buffer
	 * 
	 * @param buffer
	 *            Appending string buffer that will have the generated html appended
	 * @param choice
	 *            Choice object
	 * @param index
	 *            The index of this option
	 * @param selected
	 *            The currently selected string value
	 */
	protected void appendOptionHtml(AppendingStringBuffer buffer, Object choice, int index,
			String selected)
	{
		Object objectValue = renderer.getDisplayValue(choice);
		Class objectClass = objectValue == null ? null : objectValue.getClass();
		String displayValue = "";
		if (objectClass != null && objectClass != String.class)
		{
			displayValue = getConverter(objectClass).convertToString(objectValue, getLocale());
		}
		else if (objectValue != null)
		{
			displayValue = objectValue.toString();
		}
		buffer.append("\n<option ");
		if (isSelected(choice, index, selected))
		{
			buffer.append("selected=\"selected\" ");
		}
		if (isDisabled(choice, index, selected))
		{
			buffer.append("disabled=\"disabled\" ");
		}
		buffer.append("value=\"");
		buffer.append(Strings.escapeMarkup(renderer.getIdValue(choice, index)));
		buffer.append("\">");

		String display = displayValue;
		if (localizeDisplayValues())
		{
			display = getLocalizer().getString(displayValue, this, displayValue);
		}
		CharSequence escaped = display;
		if (getEscapeModelStrings())
		{
			escaped = Strings.escapeMarkup(display, false, true);
		}
		buffer.append(escaped);
		buffer.append("</option>");
	}

	/**
	 * @see org.apache.wicket.markup.html.form.FormComponent#supportsPersistence()
	 */
	protected boolean supportsPersistence()
	{
		return true;
	}

	/**
	 * Override this method if you want to localize the display values of the generated options. By
	 * default false is returned so that the display values of options are not tested if they have a
	 * i18n key.
	 * 
	 * @return true If you want to localize the display values, default == false
	 */
	protected boolean localizeDisplayValues()
	{
		return false;
	}

	/**
	 * Change object to represent the change of the choices property
	 * 
	 * @author ivaynberg
	 */
	private class ChoicesListChange extends Change
	{
		private static final long serialVersionUID = 1L;

		private final IModel oldChoices;

		/**
		 * Construct.
		 */
		public ChoicesListChange()
		{
			oldChoices = choices;
		}

		/**
		 * @see org.apache.wicket.version.undo.Change#undo()
		 */
		public void undo()
		{
			choices = oldChoices;
		}

		/**
		 * Make debugging easier
		 * 
		 * @see java.lang.Object#toString()
		 */
		public String toString()
		{
			return "ChoiceListChange[component: " + getPath() + ", old choices: " + oldChoices +
					"]";
		}


	}

}
