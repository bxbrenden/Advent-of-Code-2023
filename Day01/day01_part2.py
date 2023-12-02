from collections import defaultdict
from pprint import pprint
import re
import sys
from typing import Dict, List, Tuple


def usage():
    """Tell the user how to run the program."""
    raise SystemExit('USAGE: python3 day01_part2.py [INPUT_FILE]')


def read_input(inp):
    """Read the input file."""
    try:
        with open(inp, 'r') as i:
            puz = i.read().strip().split('\n')
            return puz
    except OSError:
        usage()


def first_and_last_digits(line: str) -> Dict[str, Tuple[int, int]]:
    """Given a line from the puzzle, return the first and last int. digits."""
    print(f'Searching for first and last digits in line: "{line}"')
    try:
        nums = [(i, int(x)) for (i, x) in enumerate(list(line)) if ord(x) in range(48, 58)]
        if not nums:
            output = {"first": [], "last": []}
            print("FLD")
            pprint(output)
            return output
        first_num = nums[0][1]
        first_index = nums[0][0]
        last_num = nums[-1][1]
        last_index = nums[-1][0]
        print(f'First digit {first_num} is at index {first_index}')
        print(f'Last digit {last_num} is at index {last_index}')
        output = {"first": (first_num, first_index), "last": (last_num, last_index)}
        print("FLD")
        pprint(output)
        return output
    except IndexError:
        raise SystemExit(f'No first digit found for line {line}')
    except TypeError:
        raise SystemExit('Could not convert to int.')


def first_and_last_nums(line: str) -> Dict[str, List[int]]:
    """Given a line from the puzzle, return the first and last textual numbers."""
    print(f'Searching for first and last textual numbers in line "{line}"')
    str_to_int = {
        "one": 1,
        "two": 2,
        "three": 3,
        "four": 4,
        "five": 5,
        "six": 6,
        "seven": 7,
        "eight": 8,
        "nine": 9,
    }
    output = defaultdict(list)
    for i, val in enumerate(str_to_int.keys()):
        if indices := sorted([m.start() for m in re.finditer(val, line)]):
            for ind in indices:
                val_int = str_to_int[val]
                if output["first"]:
                    assert len(output["first"]) == 2
                    if ind < output["first"][1]:
                        output["first"] = [val_int, ind]
                else:
                    output["first"] = [val_int, ind]

                if output["last"]:
                    assert len(output["last"]) == 2
                    if ind > output["last"][1]:
                        output["last"] = [val_int, ind]
                else:
                    output["last"] = [val_int, ind]
        else:
            print(f'There were no matches in line "{line}" for the num. string "{val}"')
            continue
    output = dict(output)
    if "first" not in output.keys():
        output["first"] = []
    if "last" not in output.keys():
        output["last"] = []
    print("FLN")
    pprint(output)
    return output


def get_int(first, last):
    """Squish together the first and last digits and typecast to int and return it."""
    try:
        print(f'Squishing {first} and {last} together to make {int(str(first) + str(last))}')
        return int(str(first) + str(last))
    except TypeError:
        raise SystemExit(f'Could not create int out of {first} + {last}')


def get_all_numbers(puz):
    """Solve the puzzle."""
    total = 0
    for line in puz:
        fld = first_and_last_digits(line)
        fln = first_and_last_nums(line)

        # Handle the case where both firsts exist
        if fld['first'] and fln['first']:
            assert len(fld['first']) == 2
            assert len(fln['first']) == 2
            if fld['first'][1] < fln['first'][1]:
                first = fld['first'][0]
            else:
                first = fln['first'][0]

        # Handle case where only fld['first'] exists
        elif fld['first'] and not fln['first']:
            first = fld['first'][0]

        # Handle case where only fln['first'] exists
        elif fln['first'] and not fld['first']:
            first = fln['first'][0]

        # Handle case where neither first exists by crashing
        elif not fld['first'] and not fln['first']:
            raise SystemExit(f'Could not find first digit or textual number for line {line}')

        # Handle case where both lasts exist
        if fld['last'] and fln['last']:
            assert len(fld['last']) == 2
            assert len(fln['last']) == 2
            if fld['last'][1] > fln['last'][1]:
                last = fld['last'][0]
            else:
                last = fln['last'][0]

        # Handle case where only fld['last'] exists
        if fld['last'] and not fln['last']:
            last = fld['last'][0]

        # Handle case where only fln['last'] exists
        elif fln['last'] and not fld['last']:
            last = fln['last'][0]

        # Handle case where neither last exists by crashing
        if not fld['last'] and not fln['last']:
            # This shouldn't happen...
            raise SystemExit(f'Could not find last digit or textual number for line {line}')

        i = get_int(first, last)
        print(f'Chose first value of {first} and last value of {last}')
        total += i
    return total


def main():
    try:
        input_file = sys.argv[1]
    except IndexError:
        input_file = "sample_input.txt"
    puz = read_input(input_file)
    total = get_all_numbers(puz)
    print(f'ANSWER: {total}')


if __name__ == '__main__':
    main()
