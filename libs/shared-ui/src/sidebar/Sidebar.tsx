import { FC } from "react";

export type SidebarProps = {
    list: string[];
};

export const Sidebar: FC<SidebarProps> = ({list}) => {
    return (
        <ul>
            {list.map((item, index) => (
                <li key={index}>{item}</li>
            ))}
        </ul>
    );
};