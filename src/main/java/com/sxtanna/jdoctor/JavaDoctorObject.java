package com.sxtanna.jdoctor;

import org.jsoup.select.Elements;

import java.util.Optional;

/**
 * Represents a searchable point of JavaDoc
 */
public interface JavaDoctorObject
{

	/**
	 *
	 * The raw text contained within this JavaDoc object.
	 *
	 * @return The JavaDoc text, or an empty optional if this points to no element.
	 */
	Optional<String> text();


	/**
	 *
	 * Search this object via CSS Query
	 *
	 * @param query The CSS Query
	 *
	 * @return The Elements matching this query, or an empty optional if none.
	 *
	 * @see <a href="https://jsoup.org/apidocs/index.html?org/jsoup/select/Selector.html">CSS Query Definition</a>
	 */
	Optional<Elements> find(final String query);


	/**
	 *
	 * Traverse up this object's parents <code>offset</code> times.
	 *
	 * @param offset The amount of parents to traverse.
	 *
	 * @return This object's <code>offset</code> parent, or an empty optional if unable to traverse.
	 * @throws IllegalArgumentException if <code>offset</code> is negative.
	 */
	Optional<JavaDoctorObject> head(final int offset);

	/**
	 *
	 * Traverse this object's siblings <code>offset</code> times.
	 *
	 * @param offset The amount of siblings to traverse.
	 *
	 * @return This object's <code>offset</code> sibling, or an empty optional if unable to traverse.
	 */
	Optional<JavaDoctorObject> move(final int offset);

}
