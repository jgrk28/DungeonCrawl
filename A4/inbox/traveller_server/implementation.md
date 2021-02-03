## Implementation Notes

In the specification given to us, there were some issues that caused us to decide to improve upon
the specification.

These are our changes:

- The given `Town` class specification did not have a way of keeping track of what characters are in
  the town, so we added this functionality to the `Town` class.
    - Since querying whether a character can get to another town without encountering any other
      characters is important to the requirements, it makes sense for the town to also store a list
      of the characters in the town so that the town graph can be traversed without checking each
      character separately.
- Although there was data specified for towns and characters, there was no class mentioned in the
  specification for storing the network itself.
    - So, we added the `TownNetwork` class to keep track of the towns and the characters.
        - Without this class, we would not have been able to implement the `createCharacter`
          and `createTown` methods required by the specification, as they return instances
          of `Character` and `Town` respectively.
    - We used the `Map` data type because it makes it much easier to prevent duplicate town and
      character names, as the specifications require that all town and character names must be
      unique.