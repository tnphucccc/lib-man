import api from "../services/api";
import { useEffect, useRef, useState } from "react";
import { IoMdClose } from "react-icons/io";
import { Book } from "../pages/Books";
import { Author } from "../pages/Authors";

interface UpdateBookModalProps {
  handleCloseModal: () => void;
  handleSubmit: ({title, isbn, publicationYear, authors, coverImageUrl} : { title: string, isbn: string, publicationYear: string, authors: Author, coverImageUrl: string}) => void;
  book: Book;
}

export default function UpdateBookModal({ handleCloseModal, handleSubmit, book }: UpdateBookModalProps) {

    const titleRef = useRef<HTMLInputElement>(null);
    const isbnRef = useRef<HTMLInputElement>(null);
    const yearRef = useRef<HTMLInputElement>(null);
    const authorRef = useRef<HTMLSelectElement>(null);
    const imageRef = useRef<HTMLInputElement>(null);

    const [authorList, setAuthorList] = useState<Author[]>([]);

    const handleGetAuthors = async () => {
        try {
            const res = await api.get ('/authors'); 
            if (res){
                setAuthorList(res.data);
                console.log(res.data);
            }
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        handleGetAuthors();
    }, []);

    if (!book || !authorList.length) {
        return null;
    }

  return (
    <div className="border-0 rounded-lg shadow-lg flex flex-col w-96 bg-white p-6">
        {/*header*/}
        <div className="flex items-center justify-between pb-4 pt-2 border-b-2 border-blueGray-200 rounded-t w-full">
            <h3 className="text-lg font-semibold">Updating book</h3>
            <button
            className="border-0"
            onClick={() => handleCloseModal()}
            >
            <span className="text-lg">
                <IoMdClose />
            </span>
            </button>
        </div>
        {/*end header*/}
        <div className="w-full mt-4">
            <div className="flex flex-col text-sm mb-4">
                <label className="ml-3 opacity-50">Title</label>
                <input className="mt-1 h-10 w-full rounded-lg pl-3 border-2 border-black font-semibold"
                type="text" defaultValue={book.title} ref={titleRef}/>
            </div>
            <div className="flex flex-col text-sm mb-4">
                <label className="ml-3 opacity-50">Isbn</label>
                <input className="mt-1 h-10 w-full rounded-lg pl-3 border-2 border-black font-semibold"
                type="text" defaultValue={book.isbn} ref={isbnRef}/>
            </div>
            <div className="flex flex-col text-sm mb-4">
                <label className="ml-3 opacity-50">Publication Year</label>
                <input className="mt-1 h-10 w-full rounded-lg pl-3 border-2 border-black font-semibold"
                type="text" defaultValue={book.publicationYear} ref={yearRef}/>
            </div>
            <div className="flex flex-col text-sm mb-4">
                <label className="ml-3 opacity-50">Author</label>
                <select className="mt-1 h-10 w-full rounded-lg pl-3 border-2 border-black font-semibold"
                ref={authorRef} defaultValue={book.authors[0]?.id}>
                    {authorList.map((author) => 
                        <option key={author.authorId} value={author.authorId}>      {author.name} 
                        </option>
                    )}
                </select>
            </div>
            <div className="flex flex-col text-sm mb-4">
                <label className="ml-3 opacity-50">Cover image</label>
                <input className="mt-1 h-10 w-full rounded-lg pl-3 border-2 border-black font-semibold"
                type="text" defaultValue={book.coverImageUrl} ref={imageRef}/>
            </div>
        </div>
        <div className="flex justify-end gap-2 mt-4">
            <button
            type="button"
            className=" bg-red-500 text-sm font-semibold text-white p-2 rounded-lg w-20"
            onClick={() => handleCloseModal()}
            >
            Cancel
            </button>
            <button
            type="button"
            className=" bg-green-500 text-sm font-semibold text-white p-2 rounded-lg w-20"
            onClick={() => handleSubmit({title: titleRef.current!.value, isbn: isbnRef.current!.value, publicationYear: yearRef.current!.value, authors: authorList.filter((author)=> author.authorId == Number(authorRef.current!.value))[0], coverImageUrl: imageRef.current!.value})}
            >
            Update
            </button>
        </div>
    </div>
  )
}
