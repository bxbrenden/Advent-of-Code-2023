from typing import List, Tuple


def findGroups(
    coords: List[Tuple[int, int]],
    current_group: List[Tuple[int, int]] = None,
    groups: List[List[Tuple[int, int]]] = None,
    current_index: int = 0,
) -> List[List[Tuple[int, int]]]:
    if current_index == 0:
        groups = []
        current_group = []
        current_group.append(coords[current_index])
        return findGroups(coords, current_group, groups, current_index + 1)
    else:
        current_coord = coords[current_index]
        previous_coord = coords[current_index - 1]
        if (
            current_coord[0] == previous_coord[0]
            and current_coord[1] == previous_coord[1] + 1
        ):
            current_group.append(current_coord)
            if current_index == len(coords) - 1:
                groups.append(current_group)
                return groups
            else:
                return findGroups(coords, current_group, groups, current_index + 1)
        else:
            groups.append(current_group)
            new_group = []
            new_group.append(current_coord)
            if current_index == len(coords) - 1:
                groups.append(new_group)
                return groups
            else:
                return findGroups(coords, new_group, groups, current_index + 1)


def main():
    line = ".........232.633.......................803..........................361................192............539.................973.221...340....."
    coords = [(0, i) for (i, x) in enumerate(line) if ord(x) in range(48, 58)]
    # print(coords)
    groups = findGroups(coords)
    print('\n'.join([str(g) for g in groups]))


if __name__ == "__main__":
    main()
