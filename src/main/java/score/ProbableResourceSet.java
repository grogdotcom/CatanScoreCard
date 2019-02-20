package score;

interface ProbableResourceSet extends ResourceSet {

    /**
     * How many resources of this type are probably contained in the set?
     * @param resourceType  the type of resource, like {@link CatanScore#BRICK}
     * @return the number of a kind of resource including probability of unknown
     * @see #contains(int)
     * @see #getTotal()
     */
    double getProbableAount(int resourceType);

    /**
     * could the set contain any resources of this type?
     * @param resourceType  the type of resource, like {@link CatanScore#BRICK}
     * @return true if the probable set's amount of this resource &gt; 0
     * @see #getAmount(int)
     * @see #contains(ResourceSet)
     */
    boolean mightContain(int resourceType);

    /**
     * Get the number of resource types that mighr be contained in this set:
     * {@link CatanScore#WOOD} to {@link CatanScore#ORE},
     * An empty set returns 0, a set that might contain only wheat returns 1,
     * that same set after adding wood and sheep returns 3, etc.
     * @return  The number of resource types in this set with nonzero resource counts.
     */
    int getProbableResourceTypeCount();

    /**
     * @return true if this could contain at least the resources in other
     *
     * @param other  the sub set, can be {@code null} for an empty resource subset
     * @see #contains(int)
     */
    boolean mightContains(ResourceSet other);
}
