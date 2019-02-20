package score;

public interface ResourceSet
{
    /**
     * How many resources of this type are contained in the set?
     * @param resourceType  the type of resource, like {@link CatanScore#BRICK}
     * @return the number of a kind of resource
     * @see #contains(resourceType)
     * @see #getTotal()
     */
    int getAmount(Resource resourceType);

    /**
     * Does the set contain any resources of this type?
     * @param resourceType  the type of resource, like {@link CatanScore#BRICK}
     * @return true if the set's amount of this resource &gt; 0
     * @see #getAmount(resourceType)
     * @see #contains(ResourceSet)
     */
    boolean contains(Resource resourceType);

    /**
     * Get the number of known resource types contained in this set:
     * {@link CatanScore#WOOD} to {@link CatanScore#ORE},
     * An empty set returns 0, a set containing only wheat returns 1,
     * that same set after adding wood and sheep returns 3, etc.
     * @return  The number of resource types in this set with nonzero resource counts.
     */
    int getResourceTypeCount();

    /**
     * Get the total number of resources in this set
     * @return the total number of resources
     * @see #getAmount(resourceType)
     */
    int getTotal();

    /**
     * @return true if this contains at least the resources in other
     *
     * @param other  the sub set, can be {@code null} for an empty resource subset
     * @see #contains(resourceType)
     */
    boolean contains(ResourceSet other);
}