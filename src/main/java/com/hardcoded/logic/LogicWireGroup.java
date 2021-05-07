package com.hardcoded.logic;

import java.util.*;

/**
 * A implementation of a wire group.
 * @author HardCoded
 * @since v0.1
 */
public class LogicWireGroup {
	private final LogicSystem system;
	protected final Set<Long> wires;
	protected final Set<Long> input;
	protected final Set<Long> output;
	protected final long index;
	
	protected LogicWireGroup(LogicSystem system, long index) {
		this.system = Objects.requireNonNull(system);
		
		this.index = index;
		this.wires = new HashSet<>();
		this.input = new HashSet<>();
		this.output = new HashSet<>();
	}
	
	protected void add(LogicWire wire) {
		if(wire.group != null) {
			throw new RuntimeException("Adding a wire with a connection is not allowed");
		}
		
		wires.add(wire.getIndex());
		// No error checking is done here. Everything is made in LogicSystem
		wire.group = this;
	}
	
	// Get all wires connected to this group
	public Set<Long> getWires() {
		return wires;
	}
	
	// This should be empty or size one
	// If this has any other size show this network as having an invalid state
	public Set<Long> getInput() {
		return input;
	}

	public Set<Long> getOutput() {
		return output;
	}

	// Connect two wires together
	// Make sure that group.output -> outputs does not contain itself
	public boolean connectWire(LogicWire a, LogicWire b) {
		if(a == b || a == null || b == null) return false;
		if(a.getGroup() != this) {
			throw new RuntimeException("Invalid internal state: Wire is not connected to group");
		}
		
		if(b.getGroup() == null) {
			this.add(b);
		} else if(b.getGroup() != this) {
			// Both are connected to a larger network
			// Make sure we can connect these groups together
			// Otherwise return false
			
			a.add(b.getIndex());
			b.add(a.getIndex());
			Set<Long> network = LogicWireGroup.propagate(system, a);
			
			{
				LogicWireGroup b_group = b.getGroup();
				
				for(long index : b_group.input) {
					input.add(index);
				}
				
				for(long index : b_group.output) {
					output.add(index);
				}
				
				// Combine the two networks and update the group
				wires.addAll(network);
				for(long index : network) {
					LogicWire wire = system.getWire(index);
					wire.group = this;
				}
			}
			
			return true;
		}
		
		a.add(b.getIndex());
		b.add(a.getIndex());
		return true;
	}

	public boolean disconnectWire(LogicWire a, LogicWire b) {
		if(a == b || a == null || b == null || a.getGroup() != b.getGroup()) return false;
		if(a.getGroup() != this) {
			throw new RuntimeException("Invalid internal state: Wire is not connected to group");
		}
		
		// If they are connected their groups are connected
		a.remove(b.getIndex());
		b.remove(a.getIndex());
		
		Set<Long> network_1 = new HashSet<>();
		
		// Propagate the network and if "b" is found return
		if(LogicWireGroup.propagateFind(system, a, network_1, b.getIndex())) {
			// The networks are still connected in another way
			// We do not need to disconnect the networks
			return true;
		}

		// Get all elements that resides inside "group.wires"
		// and that does not exist in "network_1"
		Set<Long> network_2 = new HashSet<>(wires);
		network_2.removeAll(network_1);
		
		// Create a new group and replace the entire network that a contains with that new group
		Set<Long> new_input = new HashSet<>();
		Set<Long> new_output = new HashSet<>();
		{
			LogicWireGroup n_group = system.new_wire_group();
			
			for(long index : this.input) {
				if(network_2.contains(index)) {
					n_group.input.add(index);
				} else {
					new_input.add(index);
				}
			}
			
			for(long index : this.output) {
				if(network_2.contains(index)) {
					n_group.output.add(index);
				} else {
					new_output.add(index);
				}
			}
			
			// Combine the two networks and update the group
			n_group.wires.addAll(network_2);
			for(long index : network_2) {
				LogicWire wire = system.getWire(index);
				wire.group = n_group;
			}
		}
		
		{ // Change this groups state
			this.input.clear();
			this.input.addAll(new_input);
			this.output.clear();
			this.output.addAll(new_output);
			this.wires.clear();
			this.wires.addAll(network_1);
		}
		
		return true;
	}

	protected void createAndReplaceGroup(LogicWireGroup old, Set<Long> set) {
		LogicWireGroup group = system.new_wire_group();
		
		for(long index : old.input) {
			if(set.contains(index)) {
				group.input.add(index);
			}
		}
		
		for(long index : old.output) {
			if(set.contains(index)) {
				group.output.add(index);
			}
		}
		
		group.wires.addAll(set);
		for(long index : set) {
			LogicWire wire = system.getWire(index);
			wire.group = group;
		}
	}


	// Propagate a wire and get all wires connected to that one
	protected static Set<Long> propagate(LogicSystem system, LogicWire wire) {
		Set<Long> visited = new HashSet<>();
		LinkedList<Long> list = new LinkedList<>(wire.getConnections());
		
		while(!list.isEmpty()) {
			long index = list.pollLast();
			if(visited.contains(index)) continue;
			
			LogicWire w1 = system.getWire(index);
			visited.add(index);
			list.addAll(w1.getConnections());
		}
		
		return visited;
	}

	// Propagate a wire and get all wires connected to that one
	protected static boolean propagateFind(LogicSystem system, LogicWire wire, Set<Long> visited, long find_index) {
		Set<Long> set = wire.getConnections();
		if(set.contains(find_index)) {
			return true;
		}
		
		LinkedList<Long> list = new LinkedList<>(set);
		while(!list.isEmpty()) {
			long index = list.pollLast();
			if(visited.contains(index)) continue;
			visited.add(index);
			
			LogicWire w1 = system.getWire(index);
			set = w1.getConnections();
			if(set.contains(find_index)) {
				return true;
			}
			
			list.addAll(set);
		}
		
		return visited.contains(find_index);
	}
}
