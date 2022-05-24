# Command Sync Server

This plugin allows the Velocity proxy to run commands based on backend input.

## Purpose

The purpose of this is to allow easy access to the Velocity proxy instance's console. This is useful for many things including anticheat punishments, message broadcasting, and more!

## IMPORTANT!

Remember to setup a firewall for port 1500 so that only backend servers can signal the proxy to run commands!

## Setup

- Install this plugin on your Velocity instance.
- Install [Command Sync Client](https://github.com/Wind-Development/CommandSyncClient) on your backend server(s).
- Configure the Command Sync Client backend server name
- Setup is now complete! 

## Usage

- Running "/sync ban windcolor" on the backend server will ban the player windcolor (with a proper punishments plugin).
- This works with all sorts of commands and supports unlimited command lengths.

