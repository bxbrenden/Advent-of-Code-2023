from typing import List, Tuple
import sys

from neo4j import GraphDatabase


NEO4J_API = "bolt://0.0.0.0:7687"
AUTH = ("", "")
DB = "memgraph"


def read_input(filename: str) -> str:
    try:
        with open(filename, "r") as f:
            puzzle = f.read().strip()
            return puzzle
    except OSError:
        raise SystemExit("USAGE: python3 day08_part1.py [INPUT_FILE]")


def run_cypher_query(query):
    """Execute a Cypher query against the Neo4j database."""
    with GraphDatabase.driver(NEO4J_API, auth=AUTH, encrypted=False) as driver:
        driver.verify_connectivity()
        print("Executing query...")
        records, summary, keys = driver.execute_query(
            query,
            # first=first,
            database_=DB,
        )

        # Get the result
        for record in records:
            print(record)


def get_instructions_and_nodes(
    puzzle: List[str],
) -> Tuple[List[str], List[Tuple[str, Tuple[str, str]]]]:
    spl = puzzle.split("\n\n")
    instrs = list(spl[0].strip())
    nodes_strs = spl[1].strip().split("\n")
    nodes = []
    for ns in nodes_strs:
        sp = ns.split(" = ")
        node_name = sp[0]
        node_instr = tuple(sp[1].replace("(", "").replace(")", "").split(", "))
        nodes.append((node_name, node_instr))

    return (instrs, nodes)


def create_base_nodes(nodes_list: List[Tuple[str, Tuple[str, str]]]):
    for node in nodes_list:
        node_name = node[0]
        run_cypher_query(f"CREATE (n:Node {{ name: '{node_name}'}})")


def create_graph_node(node: Tuple[str, Tuple[str, str]]):
    source_node = node[0]
    left_node = node[1][0]
    right_node = node[1][1]
    if source_node == left_node and source_node == right_node:
        return
    query = f"MATCH (n0: Node {{ name: '{source_node}'}})"
    query += f"MATCH (nl:Node {{ name: '{left_node}'}})"
    query += f"MATCH (nr:Node {{ name: '{right_node}'}})"
    query += "MERGE (nr)<-[:leads_to { direction: 'right'}]-(n0)-[:leads_to { direction: 'left'}]->(nl)"
    run_cypher_query(query)


def main():
    try:
        filename = sys.argv[1]
    except IndexError:
        filename = "sample_input.txt"

    puzzle = read_input(filename)
    instrs, nodes = get_instructions_and_nodes(puzzle)
    print(f"Instructions: {instrs}")
    print(f"nodes:\n{nodes}")
    create_base_nodes(nodes)
    for node in nodes:
        create_graph_node(node)


if __name__ == "__main__":
    main()
