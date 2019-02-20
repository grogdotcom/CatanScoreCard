package score

class ScalaResourceSet(wh: Int, wo: Int, sh: Int, br: Int, or: Int) extends ResourceSet {

  val resourceTypes = List(Wheat, Wood, Sheep, Brick, Ore)

  /**
    * How many resources of this type are contained in the set?
    *
    * @param resourceType the type of resource, like { @link CatanScore#BRICK}
    * @return the number of a kind of resource
    * @see #contains(int)
    * @see #getTotal()
    */
  override def getAmount(resourceType: Resource): Int = {
    resourceType match {
      case Wheat => wh
      case Wood => wo
      case Sheep => sh
      case Brick => br
      case Ore => or
    }
  }

  /**
    * Does the set contain any resources of this type?
    *
    * @param resourceType the type of resource, like { @link CatanScore#BRICK}
    * @return true if the set's amount of this resource &gt; 0
    * @see #getAmount(int)
    * @see #contains(ResourceSet)
    */
  override def contains(resourceType: Resource): Boolean = {
    getAmount(resourceType) > 0
  }

  /**
    * Get the number of known resource types contained in this set:
    * {@link CatanScore#WOOD} to {@link CatanScore#ORE},
    * An empty set returns 0, a set containing only wheat returns 1,
    * that same set after adding wood and sheep returns 3, etc.
    *
    * @return The number of resource types in this set with nonzero resource counts.
    */
  override def getResourceTypeCount: Int = {
    resourceTypes.filter(res => contains(res)).length
  }

  /**
    * Get the total number of resources in this set
    *
    * @return the total number of resources
    * @see #getAmount(int)
    */
  override def getTotal: Int = {
    resourceTypes.map(res => getAmount(res)).sum
  }

  /**
    * @return true if this contains at least the resources in other
    * @param other the sub set, can be { @code null} for an empty resource subset
    * @see #contains(int)
    */
  override def contains(other: ResourceSet): Boolean = {
    resourceTypes.forall(res => getAmount(res) >= other.getAmount(res))
  }

  def +(other:ResourceSet): ResourceSet = {
    ScalaResourceSet(
      getAmount(Wheat) + other.getAmount(Wheat),
      getAmount(Wood) + other.getAmount(Wood),
      getAmount(Sheep) + other.getAmount(Sheep),
      getAmount(Brick) + other.getAmount(Brick),
      getAmount(Ore) + other.getAmount(Ore)
    )
  }

  def -(other: ResourceSet): ResourceSet = {
    val wh = getAmount(Wheat) - other.getAmount(Wheat)
    val wo = getAmount(Wood) - other.getAmount(Wood)
    val sh = getAmount(Sheep) - other.getAmount(Sheep)
    val br = getAmount(Brick) - other.getAmount(Brick)
    val or = getAmount(Ore) - other.getAmount(Ore)

    ScalaResourceSet(
      wh = if (wh < 0) 0 else wh,
      wo = if (wo < 0) 0 else wo,
      sh = if (sh < 0) 0 else sh,
      br = if (br < 0) 0 else br,
      or = if (or < 0) 0 else or
    )
  }
}

object ScalaResourceSet {
  def empty = ScalaResourceSet()

  def apply(wh: Int = 0, wo: Int = 0, sh: Int = 0, br: Int = 0, or: Int = 0): ScalaResourceSet = {
    new ScalaResourceSet(wh, wo, sh, br, or)
  }

  implicit def apply(resMap: Map[Resource, Int]): ScalaResourceSet = {
    new ScalaResourceSet(
      wh = resMap(Wheat),
      wo = resMap(Wood),
      sh = resMap(Sheep),
      br = resMap(Brick),
      or = resMap(Ore)
    )
  }

  implicit def apply(resList: List[Resource]): ScalaResourceSet = {

    val resMap = resList.distinct.map(res => (res, resList.filter(_ == res).size)).toMap
    ScalaResourceSet(resMap)
  }

  def plus(resourceSetL: ResourceSet, resourceSetR: ResourceSet): ResourceSet = {
    resourceSetL + resourceSetR
  }

  def minus(resourceSetL: ResourceSet, resourceSetR: ResourceSet): ResourceSet = {
    resourceSetL - resourceSetR
  }

  def toMap(resourceSet: ResourceSet): Map[Resource, Int] = {
    Resource.resourceList.map { res =>
      res -> resourceSet(res)
    }.toMap
  }

  implicit def toScalaRS(resourceSet: ResourceSet): ScalaResourceSet = {
    ScalaResourceSet(
      wh = resourceSet.getAmount(Wheat),
      wo = resourceSet.getAmount(Wood),
      sh = resourceSet.getAmount(Sheep),
      br = resourceSet.getAmount(Brick),
      or = resourceSet.getAmount(Ore)
    )
  }
}
