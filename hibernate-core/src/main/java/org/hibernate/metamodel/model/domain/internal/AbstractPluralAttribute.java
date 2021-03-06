/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.metamodel.model.domain.internal;

import java.io.Serializable;
import java.util.Collection;

import org.hibernate.metamodel.model.domain.spi.PluralAttributeImplementor;
import org.hibernate.metamodel.model.domain.spi.SimpleTypeImplementor;

/**
 * @param <D> The (D)eclaring type
 * @param <C> The {@link Collection} type
 * @param <E> The type of the Collection's elements
 *
 * @author Emmanuel Bernard
 * @author Steve Ebersole
 */
public abstract class AbstractPluralAttribute<D, C, E>
		extends AbstractAttribute<D,C>
		implements PluralAttributeImplementor<D,C,E>, Serializable {

	private final Class<C> collectionClass;

	protected AbstractPluralAttribute(PluralAttributeBuilder<D,C,E,?> builder) {
		super(
				builder.getDeclaringType(),
				builder.getProperty().getName(),
				builder.getAttributeNature(),
				builder.getValueType(),
				builder.getMember()
		);

		this.collectionClass = builder.getCollectionClass();
	}

	public static <X,C,E,K> PluralAttributeBuilder<X,C,E,K> create(
			AbstractManagedType<X> ownerType,
			SimpleTypeImplementor<E> attrType,
			Class<C> collectionClass,
			SimpleTypeImplementor<K> keyType) {
		return new PluralAttributeBuilder<>( ownerType, attrType, collectionClass, keyType );
	}

	@Override
	public SimpleTypeImplementor<E> getElementType() {
		return getValueGraphType();
	}

	@Override
	@SuppressWarnings("unchecked")
	public SimpleTypeImplementor<E> getValueGraphType() {
		return (SimpleTypeImplementor<E>) super.getValueGraphType();
	}

	@Override
	public SimpleTypeImplementor<?> getKeyGraphType() {
		return null;
	}

	@Override
	public boolean isAssociation() {
		return getPersistentAttributeType() == PersistentAttributeType.ONE_TO_MANY
				|| getPersistentAttributeType() == PersistentAttributeType.MANY_TO_MANY;
	}

	@Override
	public boolean isCollection() {
		return true;
	}

	@Override
	public BindableType getBindableType() {
		return BindableType.PLURAL_ATTRIBUTE;
	}

	@Override
	public Class<E> getBindableJavaType() {
		return getElementType().getJavaType();
	}


	@Override
	public Class<C> getJavaType() {
		return collectionClass;
	}

}
