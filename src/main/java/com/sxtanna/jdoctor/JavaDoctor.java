package com.sxtanna.jdoctor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * A utility for reading JavaDocs from a URL in a pseudo-high level fashion.
 *
 */
public final class JavaDoctor implements JavaDoctorObject, AutoCloseable
{

	private final AtomicReference<String>   url = new AtomicReference<>();
	private final AtomicReference<Document> doc = new AtomicReference<>();


	private JavaDoctor(final String url)
	{
		this.url.set(url);
	}


	// /// ==== JavaDocObject ==== \\\

	/**
	 * This will return the entire HTML document
	 */
	@Override
	public Optional<String> text()
	{
		return Optional.ofNullable(this.doc.get()).map(Element::text);
	}


	@Override
	public Optional<Elements> find(final String query)
	{
		return Optional.ofNullable(this.doc.get()).map(doc -> doc.select(query));
	}


	/**
	 * This method does nothing.
	 */
	@Override
	public Optional<JavaDoctorObject> head(final int offset)
	{
		return Optional.empty();
	}

	/**
	 * This method does nothing.
	 */
	@Override
	public Optional<JavaDoctorObject> move(final int offset)
	{
		return Optional.empty();
	}
	// \\\ ==== JavaDocObject ==== ///


	// /// ==== AutoCloseable ==== \\\
	@Override
	public void close()
	{
		this.url.set(null);
		this.doc.set(null);
	}
	// \\\ ==== AutoCloseable ==== ///


	/**
	 * Inform the doctor to fetch the HTML data for this JavaDoc URL
	 */
	public void fetch()
	{
		Optional.ofNullable(this.url.get()).map(url ->
												{
													try
													{
														return Jsoup.connect(url).maxBodySize(0).timeout(5_000).get();
													}
													catch (Exception ex)
													{
														return null;
													}
												}).ifPresent(this.doc::set);
	}

	/**
	 * Utility function for {@link JavaDoctor#find(String)} that works specifically on Java types.
	 *
	 * @param name The name of the class to find.
	 *
	 * @see JavaDoctor#find(String)
	 */
	public Optional<JavaDoctorObject> findType(final String name)
	{
		return find(String.format("a span.typeNameLink:matches(^%s$)", name)).map(elements -> elements.get(0)).map(JavaDoctorObjectImpl::new);
	}

	/**
	 *
	 * Utility function for finding the description of a certain Object based on its definition location.
	 *
	 * @param object The object to find the description of.
	 */
	public Optional<JavaDoctorObject> findDesc(final JavaDoctorObject object)
	{
		return Optional.of(object).flatMap(head -> head.head(2)).flatMap(move -> move.move(1));
	}

	/**
	 *
	 * Utility function for finding the description of a certain Object based on its definition location.
	 *
	 * @param object The object to find the description of.
	 */
	public Optional<JavaDoctorObject> findDesc(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") final Optional<JavaDoctorObject> object)
	{
		return object.flatMap(this::findDesc);
	}


	// /// ==== Object ==== \\\
	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (!(o instanceof JavaDoctor))
		{
			return false;
		}

		final JavaDoctor doctor = (JavaDoctor) o;
		return Objects.equals(url.get(), doctor.url.get()) && Objects.equals(doc.get(), doctor.doc.get());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(url.get(), doc.get());
	}

	@Override
	public String toString()
	{
		return String.format("JavaDoctor[url=%s, doc=%s]", url.get(), doc.get());
	}
	// \\\ ==== Object ==== ///


	/**
	 *
	 * Create a JavaDoctor
	 *
	 * @param url The URL to use
	 * @return a JavaDoctor that points to this url
	 */
	public static JavaDoctor make(final URL url)
	{
		return new JavaDoctor(url.toString());
	}

	private static final class JavaDoctorObjectImpl implements JavaDoctorObject
	{

		private final Element element;


		private JavaDoctorObjectImpl(final Element element)
		{
			this.element = element;
		}


		@Override
		public Optional<String> text()
		{
			return Optional.ofNullable(element).map(Element::text);
		}


		@Override
		public Optional<Elements> find(final String query)
		{
			return Optional.ofNullable(element).map(doc -> doc.select(query));
		}


		@Override
		public Optional<JavaDoctorObject> head(int offset)
		{
			if (offset < 0)
			{
				throw new IllegalArgumentException(String.format("Offset value [%d] must be non-negative.", offset));
			}
			if (offset == 0 || element == null)
			{
				return Optional.of(this);
			}

			Element head = element;

			while (head.hasParent() && offset-- > 0)
			{
				head = head.parent();
			}

			if (element == head)
			{
				return Optional.of(this);
			}

			return Optional.of(new JavaDoctorObjectImpl(head));
		}

		@Override
		public Optional<JavaDoctorObject> move(final int offset)
		{
			if (offset == 0 || element == null)
			{
				return Optional.of(this);
			}

			return Optional.of(element).map(element -> element.parent().getElementsByIndexEquals(element.elementSiblingIndex() + offset)).map(Elements::first).map(JavaDoctorObjectImpl::new);
		}

	}

}
